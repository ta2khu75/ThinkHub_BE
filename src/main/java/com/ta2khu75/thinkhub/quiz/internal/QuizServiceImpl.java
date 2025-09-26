package com.ta2khu75.thinkhub.quiz.internal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.quiz.api.QuizApi;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.api.event.QuizCreatedEvent;
import com.ta2khu75.thinkhub.quiz.internal.entity.Quiz;
import com.ta2khu75.thinkhub.quiz.internal.mapper.QuizMapper;
import com.ta2khu75.thinkhub.quiz.internal.repository.QuizRepository;
import com.ta2khu75.thinkhub.quiz.required.port.QuizTagPort;
import com.ta2khu75.thinkhub.quiz.required.port.QuizUserPort;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseFileService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService.Folder;

import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

import jakarta.validation.Valid;

@Service
class QuizServiceImpl extends BaseFileService<Quiz, Long, QuizRepository, QuizMapper> implements QuizApi {
	private final ApplicationEventPublisher events;
	private final QuizTagPort tagPort;
	private final QuizUserPort userPort;

	public QuizServiceImpl(QuizRepository repository, QuizMapper mapper, FirebaseService fireBaseService,
			ApplicationEventPublisher events, QuizTagPort tagPort, QuizUserPort accountPort) {
		super(repository, mapper, fireBaseService);
		this.events = events;
		this.tagPort = tagPort;
		this.userPort = accountPort;
	}

	@Override
	public QuizResponse create(@Valid QuizRequest request, MultipartFile file) throws IOException {
		this.validateExistence(request);
		Quiz quiz = mapper.toEntity(request);
		quiz.setTagIds(getTagIds(request));
		quiz.setPostIds(this.getPostIds(request));
		quiz.setAuthorId(SecurityUtil.getCurrentUserIdDecode());
		this.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImageUrl);
		QuizResponse response = this.save(quiz);
		events.publishEvent(new QuizCreatedEvent(quiz.getAuthorId(), quiz.getId()));
		return response;
	}

	@Override
	public QuizResponse update(Long id, @Valid QuizRequest request, MultipartFile file) throws IOException {
		this.validateExistence(request);
		Quiz quiz = readEntity(id);
		mapper.update(request, quiz);
		quiz.setTagIds(this.getTagIds(request));
		quiz.setPostIds(this.getPostIds(request));
		this.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImageUrl);
		return save(quiz);
	}

	@Override
	@Transactional(readOnly = true)
	public QuizResponse read(Long id) {
		Quiz quiz = readEntity(id);
		quiz.setQuestions(null);
		QuizResponse response = mapper.convert(quiz);
		response.setAuthor(userPort.readAuthor(quiz.getAuthorId()));
		response.setTags(tagPort.readAllByIds(quiz.getTagIds()));
		return response;
	}

	@Override
	public void delete(Long id) {
		Quiz quiz = this.readEntity(id);
		quiz.setDeleted(true);
		this.save(quiz);
	}

	@Override
	@Transactional
	public PageResponse<QuizResponse> search(QuizSearch search) {
		if (search.getAuthorId() != null) {
			search.setAuthorIdQuery(decode(search.getAuthorId(), IdConfig.USER));
		}
		Long authorId = search.getAuthorIdQuery();

		// Nếu không phải là chính chủ, chỉ cho xem bài viết PUBLIC
		if (!SecurityUtil.isAuthorDecode(authorId)) {
			search.setAccessModifier(AccessModifier.PUBLIC);
		}

		Page<Quiz> page = repository.search(search);

		// Lấy toàn bộ tagIds trong trang này
		Set<Long> tagIds = page.stream().flatMap(quiz -> quiz.getTagIds().stream()).collect(Collectors.toSet());

		Map<Long, TagDto> tagMap = tagIds.isEmpty() ? Map.of()
				: tagPort.readAllByIds(tagIds).stream().collect(Collectors.toMap(TagDto::id, Function.identity()));

		// Lấy author map
		Map<Long, AuthorResponse> authorMap;
		if (authorId != null) {
			// Nếu đã biết authorId → chỉ cần 1 author
			AuthorResponse author = userPort.readAuthor(authorId);
			authorMap = Map.of(authorId, author);
		} else {
			// Lấy toàn bộ authorId trong trang
			Set<Long> authorIds = page.getContent().stream().map(Quiz::getAuthorId).collect(Collectors.toSet());

			authorMap = userPort.readMapAuthorsByUserIds(authorIds);
		}

		// Ánh xạ quiz → QuizResponse
		List<QuizResponse> responses = page.getContent().stream().map(quiz -> toResponse(quiz, authorMap, tagMap))
				.toList();

		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), responses);
	}

	private QuizResponse toResponse(Quiz quiz, Map<Long, AuthorResponse> authorMap, Map<Long, TagDto> tagMap) {
		QuizResponse response = mapper.convert(quiz);
		response.setAuthor(authorMap.get(quiz.getAuthorId()));
		Set<TagDto> tags = quiz.getTagIds().stream().map(tagMap::get).filter(Objects::nonNull)
				.collect(Collectors.toSet());
		response.setTags(tags);
		return response;
	}

	@Override
	@Transactional
	@Cacheable(cacheNames = "dynamicCache", key = "quizDetail#id")
	public QuizDetailResponse readDetail(Long id) {
		return mapper.toDetailResponse(this.readEntity(id));
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find quiz with id: " + id);
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.QUIZ;
	}

	private void validateExistence(QuizRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.CATEGORY, request.categoryId()));
		getPostIds(request).forEach(postId -> events.publishEvent(new CheckExistsEvent<>(EntityType.POST, postId)));
	}

	private Set<Long> getTagIds(QuizRequest request) {
		return request.tags().stream().map(tag -> {
			TagDto tagDto = tagPort.readByName(tag.toLowerCase());
			if (tagDto != null)
				return tagDto.id();
			return tagPort.create(tag).id();
		}).collect(Collectors.toSet());
	}
	private Set<Long> getPostIds(QuizRequest request) {
		return request.postIds().stream().map(postId -> decode(postId, IdConfig.POST)).collect(Collectors.toSet());
	}

	private QuizResponse save(Quiz quiz) {
		quiz = repository.save(quiz);
		return mapper.convert(quiz);
	}

}
