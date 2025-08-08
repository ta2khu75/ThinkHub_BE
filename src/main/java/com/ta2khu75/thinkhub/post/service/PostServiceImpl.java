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
import com.ta2khu75.thinkhub.post.PostService;
import com.ta2khu75.thinkhub.post.dto.PostRequest;
import com.ta2khu75.thinkhub.post.dto.PostResponse;
import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.post.entity.Post;
import com.ta2khu75.thinkhub.post.mapper.PostMapper;
import com.ta2khu75.thinkhub.post.repository.PostRepository;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.tag.TagService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

@Service
public class PostServiceImpl extends BaseService<Post, Long, PostRepository, PostMapper> implements PostService {
	private final ApplicationEventPublisher events;
	private final AccountService accountService;
	private final TagService tagService;

	public PostServiceImpl(PostRepository repository, PostMapper mapper, ApplicationEventPublisher events,
			AccountService accountService, TagService tagService) {
		super(repository, mapper);
		this.events = events;
		this.accountService = accountService;
		this.tagService = tagService;
	}

	@Override
	public PostResponse create(@Valid PostRequest request) {
		validateExistence(request);
		Post post = mapper.toEntity(request);
		post.setTagIds(this.getTagIds(request));
		post.setQuizIds(request.quizIds().stream().map(quizId-> decode(quizId, IdConfig.QUIZ)).collect(Collectors.toSet()));
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
	}

	private Set<Long> getTagIds(PostRequest request) {
		return request.tags().stream().map(tag -> {
			TagDto tagDto = tagService.readByName(tag.toLowerCase());
			if (tagDto != null)
				return tagDto.id();
			return tagService.create(new TagDto(null, tag)).id();
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
		List<PostResponse> responses = page.getContent().stream()
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
	public PostResponse readDetail(Long id) {
		Post post = readEntity(id);
		PostResponse response = mapper.convert(readEntity(id));
		AuthorResponse author = accountService.readAuthor(post.getAuthorId());
		Set<TagDto> tags = tagService.readAllByIds(post.getTagIds());
		response.setAuthor(author);
		response.setTags(tags);
		return response;
}
}
