package com.ta2khu75.thinkhub.post.service;

import java.util.Map;
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
		String authorId = search.getAuthorId();
		if (!SecurityUtil.isAuthor(authorId))
			search.setAccessModifier(AccessModifier.PUBLIC);
		Page<Post> page = repository.search(search);
		PageResponse<PostResponse> response = mapper.toPageResponse(page);
		Set<Long> tagIds = page.stream().flatMap(post -> post.getTagIds().stream()).collect(Collectors.toSet());
		Set<TagDto> tags = tagIds.isEmpty() ? Set.of() : tagService.readAllByIds(tagIds);
		Map<Long, TagDto> tagMap = tags.stream().collect(Collectors.toMap(TagDto::id, Function.identity()));
		if (authorId != null) {
			Long accountId = decode(authorId, IdConfig.ACCOUNT);
			search.setAuthorIdQuery(accountId);
			AuthorResponse author = accountService.readAuthor(accountId);
			response.getContent().stream().forEach(post -> {
				post.setAuthor(author);
				Set<TagDto> tagDtos = post.getTags().stream().map(tag -> tagMap.get(tag.id()))
						.collect(Collectors.toSet());
				post.setTags(tagDtos);
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
			response.getContent().stream().forEach(post -> {
				AuthorResponse author = authorMap.get(post.getAuthorId());
				post.setAuthor(author);
				post.setAuthorId(null);
				Set<TagDto> tagDtos = post.getTags().stream().map(tag -> tagMap.get(tag.id()))
						.collect(Collectors.toSet());
				post.setTags(tagDtos);
			});
		}
		return response;
	}
}
