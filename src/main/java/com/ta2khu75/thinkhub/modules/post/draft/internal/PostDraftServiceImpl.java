package com.ta2khu75.thinkhub.modules.post.draft.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftCreateRequest;
import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftResponse;
import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftUpdateRequest;
import com.ta2khu75.thinkhub.modules.post.draft.internal.domain.PostDraft;
import com.ta2khu75.thinkhub.modules.post.draft.internal.infra.redis.PostDraftCache;
import com.ta2khu75.thinkhub.modules.post.draft.internal.mapper.PostDraftMapper;
import com.ta2khu75.thinkhub.modules.post.draft.internal.service.PostDraftService;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostDraftServiceImpl implements PostDraftService {
	private final PostDraftMapper mapper;
	private final PostDraftCache cache;

	@Override
	public PostDraftResponse create(PostDraftCreateRequest request) {
		String id = UUID.randomUUID().toString();
		Long ownerId = SecurityUtil.getCurrentUserIdDecode();
		PostDraft post = mapper.toDomain(request);
		post.setId(id);
		post.setOwnerId(ownerId);
		cache.save(id, post);
		return mapper.convert(post);
	}

	@Override
	public void update(String id, PostDraftUpdateRequest request) {
		PostDraft post = cache.get(id);
		if (post.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		mapper.update(request, post);
		cache.save(id, post);
	}

	@Override
	public PostDraftResponse read(String id) {
		PostDraft post = cache.get(id);
		if (post.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		return mapper.convert(post);
	}

	@Override
	public void delete(String id) {
		PostDraft post = cache.get(id);
		if (post.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		cache.delete(id);
	}

}
