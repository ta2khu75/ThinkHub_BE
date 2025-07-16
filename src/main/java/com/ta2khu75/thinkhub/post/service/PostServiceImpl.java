package com.ta2khu75.thinkhub.post.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.service.CommentServiceImpl;
import com.ta2khu75.thinkhub.post.PostService;
import com.ta2khu75.thinkhub.post.dto.PostRequest;
import com.ta2khu75.thinkhub.post.dto.PostResponse;
import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.post.entity.Post;
import com.ta2khu75.thinkhub.post.mapper.PostMapper;
import com.ta2khu75.thinkhub.post.repository.PostRepository;
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
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.tag.TagService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

@Service
public class PostServiceImpl extends BaseService<Post, Long, PostRepository, PostMapper> implements PostService {

	private final CommentService commentService;
	private final ApplicationEventPublisher events;
	private final AccountService accountService;
	private final TagService tagService;
	private final ReportService reportService;

	public PostServiceImpl(PostRepository repository, PostMapper mapper, ApplicationEventPublisher events,
			AccountService accountService, TagService tagService, CommentServiceImpl commentService,
			ReportService reportService) {
		super(repository, mapper);
		this.events = events;
		this.accountService = accountService;
		this.tagService = tagService;
		this.commentService = commentService;
		this.reportService = reportService;
	}

	@Override
	public PostResponse create(@Valid PostRequest request) {
		validateExistence(request);
		Post post = mapper.toEntity(request);
		post.setTagIds(this.getTagIds(request));
		post.setAuthorId(SecurityUtil.getCurrentAccountIdDecode());
		return this.save(post);
	}

	@Override
	public PostResponse update(Long id, @Valid PostRequest request) {
		validateExistence(request);
		Post post = this.readEntity(id);
		mapper.update(request, post);
		post.setTagIds(this.getTagIds(request));
		return this.save(post);
	}

	@Override
	public PostResponse read(Long id) {
		return mapper.convert(readEntity(id));
	}

	@Override
	public void delete(Long id) {
		Post post = readEntity(id);
		post.setDeleted(true);
		repository.save(post);
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find post with id: " + id);
		}

	}

	@Override
	public EntityType getEntityType() {
		return EntityType.POST;
	}

	private void validateExistence(PostRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.CATEGORY, request.categoryId()));
		request.quizIds().forEach(
				quizId -> events.publishEvent(new CheckExistsEvent<>(EntityType.QUIZ, decode(quizId, IdConfig.QUIZ))));
		request.tags().forEach(tag -> {
			if (tag.id() != null)
				events.publishEvent(new CheckExistsEvent<>(EntityType.TAG, tag.id()));

		});
	}

	private Set<Long> getTagIds(PostRequest request) {
		return request.tags().stream().map(tag -> {
			if (tag.id() != null) {
				return tag.id();
			}
			return tagService.create(tag).id();
		}).collect(Collectors.toSet());
	}

	private PostResponse save(Post post) {
		post = repository.save(post);
		return mapper.convert(post);
	}

	@Override
	public PageResponse<PostResponse> search(PostSearch search) {
		if (search.getAuthorId() != null) {
			search.setAuthorIdQuery(decode(search.getAuthorId(), IdConfig.ACCOUNT));
		}
		Long authorId = search.getAuthorIdQuery();

		// Nếu không phải là chính chủ, chỉ cho xem bài viết PUBLIC
		if (!SecurityUtil.isAuthorDecode(authorId)) {
			search.setAccessModifier(AccessModifier.PUBLIC);
		}

		Page<Post> page = repository.search(search);

		// Lấy toàn bộ tagIds trong trang này
		Set<Long> tagIds = page.stream().flatMap(post -> post.getTagIds().stream()).collect(Collectors.toSet());

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
			Set<Long> authorIds = page.getContent().stream().map(Post::getAuthorId).collect(Collectors.toSet());

			authorMap = accountService.readMapAuthorsByAccountIds(authorIds);
		}

		// Ánh xạ post → PostResponse
		List<PostResponse> responses = page.getContent().parallelStream()
				.map(post -> toResponse(post, authorMap, tagMap)).toList();

		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), responses);
	}

	private PostResponse toResponse(Post post, Map<Long, AuthorResponse> authorMap, Map<Long, TagDto> tagMap) {
		PostResponse response = mapper.convert(post);
		response.setAuthor(authorMap.get(post.getAuthorId()));

		Set<TagDto> tags = post.getTagIds().stream().map(tagMap::get).filter(Objects::nonNull)
				.collect(Collectors.toSet());

		response.setTags(tags);
		return response;
	}

	@Override
	public CommentResponse comment(Long id, CommentRequest request) {
		return commentService.create(id, CommentTargetType.POST, request);
	}

	@Override
	public PageResponse<CommentResponse> readPageComments(Long targetId, Search search) {
		return commentService.readPageBy(targetId, CommentTargetType.POST, search);
	}

	@Override
	public ReportResponse report(Long id, ReportRequest request) {
		return reportService.create(id, ReportTargetType.POST, request);
	}
}
