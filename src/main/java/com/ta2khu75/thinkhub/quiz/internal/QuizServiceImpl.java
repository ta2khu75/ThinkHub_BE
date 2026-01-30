package com.ta2khu75.thinkhub.quiz.internal;

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

import com.ta2khu75.thinkhub.quiz.api.QuizApi;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.api.event.QuizCreatedEvent;
import com.ta2khu75.thinkhub.quiz.internal.entity.Quiz;
import com.ta2khu75.thinkhub.quiz.internal.entity.QuizStatus;
import com.ta2khu75.thinkhub.quiz.internal.mapper.QuizMapper;
import com.ta2khu75.thinkhub.quiz.internal.repository.QuizRepository;
import com.ta2khu75.thinkhub.quiz.internal.service.QuizService;
import com.ta2khu75.thinkhub.quiz.internal.validator.QuizValidator;
import com.ta2khu75.thinkhub.quiz.required.port.QuizMediaPort;
import com.ta2khu75.thinkhub.quiz.required.port.QuizTagPort;
import com.ta2khu75.thinkhub.quiz.required.port.QuizUserPort;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

import jakarta.validation.Valid;

@Service
class QuizServiceImpl extends BaseService<Quiz, Long, QuizRepository> implements QuizApi, QuizService, IdDecodable {

	public QuizServiceImpl(QuizRepository repository, QuizMapper mapper, ApplicationEventPublisher events,
			QuizTagPort tagPort, QuizUserPort userPort, QuizMediaPort mediaPort, QuizValidator validator) {
		super(repository);
		this.events = events;
		this.tagPort = tagPort;
		this.userPort = userPort;
		this.mediaPort = mediaPort;
		this.validator = validator;
		this.mapper = mapper;
	}

	private final QuizMapper mapper;
	private final QuizTagPort tagPort;
	private final QuizUserPort userPort;
	private final QuizMediaPort mediaPort;
	private final QuizValidator validator;
	private final ApplicationEventPublisher events;

	@Override
	public QuizResponse create(@Valid QuizRequest request) {
		this.validateExistence(request);
		Quiz quiz = mapper.toEntity(request);
		quiz.setTagIds(getTagIds(request));
		quiz.setPostIds(this.getPostIds(request));
		quiz.setOwnerId(SecurityUtil.getCurrentUserIdDecode());
		QuizResponse response = this.save(quiz);
		events.publishEvent(new QuizCreatedEvent(response.getAuthor().id(), quiz.getId()));
		return response;
	}

	@Override
	public QuizResponse update(String id, @Valid QuizRequest request) {
		Long quizId = decodeId(id);
		this.validateExistence(request);
		Quiz quiz = readEntity(quizId);
		mapper.update(request, quiz);
		quiz.setTagIds(this.getTagIds(request));
		quiz.setPostIds(this.getPostIds(request));
		return save(quiz);
	}

	@Override
	@Transactional(readOnly = true)
	public QuizResponse read(String id) {
		Long quizId = decodeId(id);
		return read(quizId);
	}

	@Override
	@Transactional(readOnly = true)
	public QuizResponse read(Long id) {
		Quiz quiz = readEntity(id);
		QuizResponse response = mapper.convert(quiz);
		response.setAuthor(userPort.readAuthor(quiz.getOwnerId()));
		response.setTags(tagPort.readAllByIds(quiz.getTagIds()));
		return response;
	}

	@Override
	@Transactional
	public PageResponse<QuizResponse> search(QuizSearch search) {
		if (search.getOwnerId() != null) {
			search.setOwnerIdQuery(decode(search.getOwnerId(), IdConfig.USER));
		}
		Long authorId = search.getOwnerIdQuery();

		// Nếu không phải là chính chủ, chỉ cho xem bài viết PUBLIC
		if (!SecurityUtil.isAuthorDecode(authorId)) {
			search.setStatus(QuizStatus.ACTIVE);
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
			Set<Long> authorIds = page.getContent().stream().map(Quiz::getOwnerId).collect(Collectors.toSet());

			authorMap = userPort.readMapAuthorsByUserIds(authorIds);
		}

		// Ánh xạ quiz → QuizResponse
		List<QuizResponse> responses = page.getContent().stream().map(quiz -> toResponse(quiz, authorMap, tagMap))
				.toList();

		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), responses);
	}

	private QuizResponse toResponse(Quiz quiz, Map<Long, AuthorResponse> authorMap, Map<Long, TagDto> tagMap) {
		QuizResponse response = mapper.convert(quiz);
		response.setAuthor(authorMap.get(quiz.getOwnerId()));
		Set<TagDto> tags = quiz.getTagIds().stream().map(tagMap::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		response.setTags(tags);
		if (quiz.getMediaId() != null) {
			String url = mediaPort.read(quiz.getMediaId()).url();
			response.setImageUrl(url);
		}
		return response;
	}

	@Override
	@Transactional
	@Cacheable(cacheNames = "dynamicCache", key = "quizDetail#id")
	public QuizDetailResponse readDetail(String id) {
		Long quizId = decodeId(id);
		return mapper.toDetailResponse(this.readEntity(quizId));
	}

	@Override
	@Transactional
	public QuizDetailResponse readDetail(Long id) {
		return mapper.toDetailResponse(this.readEntity(id));
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.QUIZ;
	}

	private void validateExistence(QuizRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.CATEGORY, request.categoryId()));
		getPostIds(request).forEach(postId -> events.publishEvent(new CheckExistsEvent<>(EntityType.POST, postId)));
		if (request.mediaId() != null) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.mediaId()));
		}
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

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}

	@Override
	public void ensureExists(String id) {
		Long quizId = decodeId(id);
		this.assertExists(quizId);
	}

	@Transactional
	private Quiz updateStatus(String id, QuizStatus status) {
		Long quizId = decodeId(id);
		Quiz quiz = readEntity(quizId);
		quiz.setStatus(status);
		return repository.save(quiz);
	}

	@Override
	public void disable(String id) {
		this.updateStatus(id, QuizStatus.ADMIN_DISABLED);
	}

	@Override
	public void publish(String id) {
		this.updateStatus(id, QuizStatus.ACTIVE);

	}

	@Override
	public void hide(String id) {
		this.updateStatus(id, QuizStatus.INACTIVE);
	}

	@Override
	public void delete(String id) {
		this.updateStatus(id, QuizStatus.OWNER_DELETED);
	}
}
