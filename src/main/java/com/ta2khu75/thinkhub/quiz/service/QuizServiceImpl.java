package com.ta2khu75.thinkhub.quiz.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;
import com.ta2khu75.thinkhub.quiz.mapper.QuizMapper;
import com.ta2khu75.thinkhub.quiz.repository.QuizRepository;
import com.ta2khu75.thinkhub.report.ReportService;
import com.ta2khu75.thinkhub.report.ReportTargetType;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
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
import com.ta2khu75.thinkhub.tag.TagService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

@Service
public class QuizServiceImpl extends BaseFileService<Quiz, Long, QuizRepository, QuizMapper> implements QuizService {
	private final ApplicationEventPublisher events;
	private final TagService tagService;
	private final AccountService accountService;
	private final CommentService commentService;
	private final ReportService reportService;

	public QuizServiceImpl(QuizRepository repository, QuizMapper mapper, FirebaseService fireBaseService,
			ApplicationEventPublisher events, TagService tagService, AccountService accountService,
			CommentService commentService, ReportService reportService) {
		super(repository, mapper, fireBaseService);
		this.events = events;
		this.tagService = tagService;
		this.accountService = accountService;
		this.commentService = commentService;
		this.reportService = reportService;
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
		if (search.getAuthorId() != null) {
			search.setAuthorIdQuery(decode(search.getAuthorId(), IdConfig.ACCOUNT));
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
				: tagService.readAllByIds(tagIds).stream().collect(Collectors.toMap(TagDto::id, Function.identity()));

		// Lấy author map
		Map<Long, AuthorResponse> authorMap;
		if (authorId != null) {
			// Nếu đã biết authorId → chỉ cần 1 author
			AuthorResponse author = accountService.readAuthor(authorId);
			authorMap = Map.of(authorId, author);
		} else {
			// Lấy toàn bộ authorId trong trang
			Set<Long> authorIds = page.getContent().stream().map(Quiz::getAuthorId).collect(Collectors.toSet());

			authorMap = accountService.readMapAuthorsByAccountIds(authorIds);
		}

		// Ánh xạ quiz → QuizResponse
		List<QuizResponse> responses = page.getContent().parallelStream()
				.map(quiz -> toResponse(quiz, authorMap, tagMap)).toList();

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

	@Override
	public CommentResponse comment(Long id, CommentRequest request) {
		return commentService.create(id, CommentTargetType.QUIZ, request);
	}

	@Override
	public PageResponse<CommentResponse> readPageComments(Long targetId, Search search) {
		return commentService.readPageBy(targetId, CommentTargetType.QUIZ, search);
	}

	@Override
	public ReportResponse report(Long id, ReportRequest request) {
		return reportService.create(id, ReportTargetType.QUIZ, request);
	}
}
