package com.ta2khu75.thinkhub.modules.post.internal;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.post.api.PostApi;
import com.ta2khu75.thinkhub.modules.post.api.dto.PostRequest;
import com.ta2khu75.thinkhub.modules.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.modules.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.modules.post.api.event.PostCreatedEvent;
import com.ta2khu75.thinkhub.modules.post.internal.entity.Post;
import com.ta2khu75.thinkhub.modules.post.internal.entity.PostStatus;
import com.ta2khu75.thinkhub.modules.post.internal.mapper.PostMapper;
import com.ta2khu75.thinkhub.modules.post.internal.repository.PostRepository;
import com.ta2khu75.thinkhub.modules.post.internal.service.PostService;
import com.ta2khu75.thinkhub.modules.post.internal.validator.PostErrorCode;
import com.ta2khu75.thinkhub.modules.post.required.client.PostMediaPort;
import com.ta2khu75.thinkhub.modules.post.required.client.PostTagPort;
import com.ta2khu75.thinkhub.modules.post.required.client.PostUserPort;
import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.domain.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.BusinessException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import jakarta.validation.Valid;

@Service
class PostServiceImpl extends BaseService<Post, Long, PostRepository> implements PostService, PostApi, IdDecodable {
	private final ApplicationEventPublisher events;
	private final PostUserPort userPort;
	private final PostTagPort tagPort;
	private final PostMediaPort mediaPort;
	private final PostMapper mapper;

	public PostServiceImpl(PostRepository repository, PostMapper mapper, ApplicationEventPublisher events,
			PostUserPort userPort, PostTagPort tagPort, PostMediaPort mediaPort) {
		super(repository);
		this.events = events;
		this.userPort = userPort;
		this.tagPort = tagPort;
		this.mediaPort = mediaPort;
		this.mapper = mapper;
	}

	@Override
	public PostResponse create(@Valid PostRequest request) {
		this.validateExistence(request);
		if (PostStatus.OWNER_DELETED.equals(request.status()) || PostStatus.ADMIN_DISABLED.equals(request.status())) {
			throw new BusinessException(PostErrorCode.STATUS_INVALID, "Invalid status");
		}
		Post post = mapper.toEntity(request);
		post.setTagIds(this.getTagIds(request));
		post.setQuizIds(this.getQuizIds(request));
		post.setOwnerId(SecurityUtil.getCurrentUserIdDecode());
		PostResponse response = this.save(post);
		events.publishEvent(new PostCreatedEvent(response.getAuthor().id(), post.getId()));
		return response;
	}

	@Override
	public PostResponse update(String id, @Valid PostRequest request) {
		validateExistence(request);
		Long postId = decodeId(id);
		Post post = this.readEntity(postId);
		if (post.getStatus().equals(PostStatus.OWNER_DELETED) || post.getStatus().equals(PostStatus.ADMIN_DISABLED)) {
			throw new BusinessException(PostErrorCode.STATUS_INVALID, "Invalid status");
		}
		mapper.update(request, post);
		post.setTagIds(this.getTagIds(request));
		post.setQuizIds(this.getQuizIds(request));
		return this.save(post);
	}

	@Override
	public PostResponse read(String id) {
		Long postId = decodeId(id);
		return mapper.convert(readEntity(postId));
	}

	@Override
	public PostResponse read(Long id) {
		return mapper.convert(readEntity(id));
	}

	@Override
	public void delete(String id) {
		Long postId = decodeId(id);
		Post post = readEntity(postId);
		post.setStatus(PostStatus.OWNER_DELETED);
		repository.save(post);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.POST;
	}

	private void validateExistence(PostRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.CATEGORY, request.categoryId()));
		this.getQuizIds(request)
				.forEach(quizId -> events.publishEvent(new CheckExistsEvent<>(EntityType.QUIZ, quizId)));
		if (request.mediaId() != null) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.mediaId()));
		}
	}

	private Set<Long> getTagIds(PostRequest request) {
		return request.tags().stream().map(tag -> {
			TagDto tagDto = tagPort.readByName(tag.toLowerCase());
			if (tagDto != null)
				return tagDto.id();
			return tagPort.create(tag).id();
		}).collect(Collectors.toSet());
	}

	private Set<Long> getQuizIds(PostRequest request) {
		return request.quizIds().stream().map(quizId -> decode(quizId, IdConfig.QUIZ)).collect(Collectors.toSet());
	}

	private PostResponse save(Post post) {
		post = repository.save(post);
		return mapper.convert(post);
	}

	@Override
	public PageResponse<PostResponse> search(PostSearch search) {
		if (search.getOwnerId() != null) {
			search.setOwnerIdQuery(decode(search.getOwnerId(), IdConfig.USER));
		}
		Long authorId = search.getOwnerIdQuery();

		// Nếu không phải là chính chủ, chỉ cho xem bài viết PUBLIC
		if (!SecurityUtil.isAuthorDecode(authorId)) {
			search.setStatus(PostStatus.ACTIVE);
		}

		Page<Post> page = repository.search(search);

		// Lấy toàn bộ tagIds trong trang này
		Set<Long> tagIds = page.stream().flatMap(post -> post.getTagIds().stream()).collect(Collectors.toSet());

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
			Set<Long> authorIds = page.getContent().stream().map(Post::getOwnerId).collect(Collectors.toSet());

			authorMap = userPort.readMapAuthorsByUserIds(authorIds);
		}

		// Ánh xạ post → PostResponse
		List<PostResponse> responses = page.getContent().stream().map(post -> toResponse(post, authorMap, tagMap))
				.toList();

		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), responses);
	}

	private PostResponse toResponse(Post post, Map<Long, AuthorResponse> authorMap, Map<Long, TagDto> tagMap) {
		PostResponse response = mapper.convert(post);
		response.setAuthor(authorMap.get(post.getOwnerId()));

		Set<TagDto> tags = post.getTagIds().stream().map(tagMap::get).filter(Objects::nonNull)
				.collect(Collectors.toSet());
		response.setTags(tags);

		if (post.getMediaId() != null) {
			String url = mediaPort.read(post.getMediaId()).url();
			response.setImageUrl(url);
		}

		return response;
	}

	@Override
	public PostResponse readDetail(String id) {
		Long postId = decodeId(id);
		Post post = readEntity(postId);
		PostResponse response = mapper.convert(readEntity(postId));
		AuthorResponse author = userPort.readAuthor(post.getOwnerId());
		Set<TagDto> tags = tagPort.readAllByIds(post.getTagIds());
		response.setAuthor(author);
		response.setTags(tags);
		return response;
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public void ensureExists(String id) {
		Long postId = decodeId(id);
		this.assertExists(postId);
	}

	@Override
	public void disable(String id) {
		Long postId = decodeId(id);
		Post post = readEntity(postId);
		post.setStatus(PostStatus.ADMIN_DISABLED);
		repository.save(post);

	}

}
