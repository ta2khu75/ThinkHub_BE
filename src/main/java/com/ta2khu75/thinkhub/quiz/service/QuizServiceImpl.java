package com.ta2khu75.thinkhub.quiz.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;
import com.ta2khu75.thinkhub.quiz.mapper.QuizMapper;
import com.ta2khu75.thinkhub.quiz.repository.QuizRepository;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseFileService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService.Folder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.tag.TagService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

@Service
public class QuizServiceImpl extends BaseFileService<Quiz, Long, QuizRepository, QuizMapper> implements QuizService {
	private final ApplicationEventPublisher events;
	private final TagService tagService;
	private final AccountService accountService;

	public QuizServiceImpl(QuizRepository repository, QuizMapper mapper, FirebaseService fireBaseService,
			ApplicationEventPublisher events, TagService tagService, AccountService accountService) {
		super(repository, mapper, fireBaseService);
		this.events = events;
		this.tagService = tagService;
		this.accountService = accountService;
	}

	@Override
	public QuizResponse create(@Valid QuizRequest request, MultipartFile file) throws IOException {
		validateExistence(request);
		Quiz quiz = mapper.toEntity(request);
		quiz.setTagIds(getTagIds(request));
		quiz.setAuthorId(SecurityUtil.getCurrentAccountIdDecode());
		this.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImageUrl);
		return this.save(quiz);
	}

	@Override
	public QuizResponse update(Long id, @Valid QuizRequest request, MultipartFile file) throws IOException {
		this.validateExistence(request);
		Quiz quiz = readEntity(id);
		mapper.update(request, quiz);
		quiz.setTagIds(this.getTagIds(request));
		this.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImageUrl);
		return save(quiz);
	}

	@Override
	public QuizResponse read(Long id) {
		Quiz quiz = readEntity(id);
		quiz.setQuestions(null);
		return mapper.convert(quiz);
	}

	@Override
	public void delete(Long id) {
		Quiz quiz = this.readEntity(id);
		quiz.setDeleted(true);
		this.save(quiz);
	}

	@Override
	public PageResponse<QuizResponse> search(QuizSearch search) {
		String authorId = search.getAuthorId();
		if (!SecurityUtil.isAuthor(search.getAuthorId()))
			search.setAccessModifier(AccessModifier.PUBLIC);
		Page<Quiz> page = repository.search(search);
		PageResponse<QuizResponse> response = mapper.toPageResponse(page);
		Set<Long> tagIds = page.stream().flatMap(post -> post.getTagIds().stream()).collect(Collectors.toSet());
		Set<TagDto> tags = tagIds.isEmpty() ? Set.of() : tagService.readAllByIds(tagIds);
		Map<Long, TagDto> tagMap = tags.stream().collect(Collectors.toMap(TagDto::id, Function.identity()));
		if (authorId != null) {
			Long accountId = decode(authorId, IdConfig.ACCOUNT);
			search.setAuthorIdQuery(accountId);
			AuthorResponse author = accountService.readAuthor(accountId);
			response.getContent().stream().forEach(quiz -> {
				quiz.setAuthor(author);
				Set<TagDto> tagDtos = quiz.getTags().stream().map(tag -> tagMap.get(tag.id()))
						.collect(Collectors.toSet());
				quiz.setTags(tagDtos);
			});
		}

		if (authorId == null) {
			Set<Long> authorIds = page.getContent().stream().map(post -> post.getAuthorId())
					.collect(Collectors.toSet());
			Set<AuthorResponse> authors = accountService.readAllAuthorsByAccountIds(authorIds);
			Map<Long, AuthorResponse> authorMap = authors.stream()
					.collect(Collectors.toMap(AuthorResponse::getOriginalId, a -> {
						a.setOriginalId(null);
						return a;
					}));
			response.getContent().stream().forEach(quiz -> {
				AuthorResponse author = authorMap.get(quiz.getAuthorId());
				quiz.setAuthor(author);
				quiz.setAuthorId(null);
				Set<TagDto> tagDtos = quiz.getTags().stream().map(tag -> tagMap.get(tag.id()))
						.collect(Collectors.toSet());
				quiz.setTags(tagDtos);
			});
		}
		return response;
	}

	@Override
	public QuizResponse readDetail(Long id) {
		return mapper.convert(readEntity(id));
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
		request.postIds().forEach(
				postId -> events.publishEvent(new CheckExistsEvent<>(EntityType.POST, decode(postId, IdConfig.POST))));
		request.tags().forEach(tag -> {
			if (tag.id() != null)
				events.publishEvent(new CheckExistsEvent<>(EntityType.TAG, tag.id()));

		});
	}

	private Set<Long> getTagIds(QuizRequest request) {
		return request.tags().stream().map(tag -> {
			if (tag.id() != null) {
				return tag.id();
			}
			return tagService.create(tag).id();
		}).collect(Collectors.toSet());
	}

	private QuizResponse save(Quiz quiz) {
		quiz = repository.save(quiz);
		return mapper.convert(quiz);
	}
}
