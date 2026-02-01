package com.ta2khu75.thinkhub.modules.follow.internal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.modules.follow.api.FollowApi;
import com.ta2khu75.thinkhub.modules.follow.api.FollowDirection;
import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.modules.follow.internal.entity.Follow;
import com.ta2khu75.thinkhub.modules.follow.internal.entity.FollowId;
import com.ta2khu75.thinkhub.modules.follow.internal.repository.FollowRepository;
import com.ta2khu75.thinkhub.modules.follow.internal.validator.FollowValidator;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.domain.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class FollowServiceImpl implements FollowApi, IdDecodable {
	private final FollowRepository repository;
	private final FollowValidator validator;
	private final UserApi accountApi;
	private final ApplicationEventPublisher events;

	@Override
	@Transactional
	public void follow(String userId) {
		Long followingId = decodeId(userId);
		Long followerId = SecurityUtil.getCurrentUserIdDecode();
		events.publishEvent(new CheckExistsEvent<>(EntityType.USER, userId));
		FollowId id = new FollowId(followingId, followerId);
		boolean alreadyFollowed = repository.existsById(new FollowId(followingId, followerId));
		validator.validateFollow(followerId, followingId, alreadyFollowed);
		Follow follow = new Follow(id, null);
		repository.save(follow);
	}

	@Override
	@Transactional
	public void unFollow(String userId) {
		Long followerId = SecurityUtil.getCurrentUserIdDecode();
		Long followingId = decodeId(userId);
		repository.deleteById(new FollowId(followingId, followerId));
	}

	@Override
	public PageResponse<AuthorResponse> readAuthorPage(String userId, FollowDirection direction, Search search) {
		return mapToAuthorResponse(this.readPage(userId, direction, search));
	}

	private PageResponse<AuthorResponse> mapToAuthorResponse(PageResponse<FollowResponse> pageResponse) {
		List<Long> accountIds = pageResponse.getContent().stream().map(FollowResponse::id).toList();
		List<AuthorResponse> authors = new ArrayList<>(accountApi.readMapAuthorsByUserIds(accountIds).values());
		return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalElements(), pageResponse.getPage(),
				authors);
	}

	@Override
	public FollowStatusResponse isFollowing(String userId) {
		Long followerId = SecurityUtil.getCurrentUserIdDecode();
		Long followingId = decodeId(userId);
		return new FollowStatusResponse(repository.existsById(new FollowId(followingId, followerId)));
	}

	@Override
	public PageResponse<FollowResponse> readPage(String userIdString, FollowDirection direction, Search search) {
		Long userId = decodeId(userIdString);
		Pageable pageable = search.toPageable();
		Page<Follow> page = direction.query(repository, userId, pageable);
		List<FollowResponse> data = page.getContent().stream().map(f -> new FollowResponse(direction.extractUserId(f)))
				.toList();

		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), data);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}
}
