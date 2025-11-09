package com.ta2khu75.thinkhub.follow.internal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.follow.api.FollowApi;
import com.ta2khu75.thinkhub.follow.api.FollowDirection;
import com.ta2khu75.thinkhub.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.follow.internal.entity.Follow;
import com.ta2khu75.thinkhub.follow.internal.entity.FollowId;
import com.ta2khu75.thinkhub.follow.internal.repository.FollowRepository;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.UserApi;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class FollowServiceImpl implements FollowApi, IdDecodable {
	private final FollowRepository repository;
	private final UserApi accountApi;
	private final ApplicationEventPublisher events;

	@Override
	@Transactional
	public void follow(String userId) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.USER, userId));
		Long followerId = SecurityUtil.getCurrentUserIdDecode();
		Long followingId = decodeId(userId);
		FollowId id = new FollowId(followingId, followerId);
		if (followerId.equals(followingId)) {
			throw new InvalidDataException("Cannot follow yourself");
		}
		if (!repository.existsById(id)) {
			Follow follow = new Follow(id, null);
			repository.save(follow);
		}
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
		Page<Follow> page;
		PageResponse<FollowResponse> pageResponse;
		if (direction == FollowDirection.FOLLOWING) {
			page = repository.findByIdFollowingId(userId, pageable);
			List<FollowResponse> followerResponse = page.getContent().stream()
					.map(follow -> new FollowResponse(follow.getId().getFollowerId())).toList();
			pageResponse = new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(),
					followerResponse);
		} else {
			page = repository.findByIdFollowerId(userId, pageable);
			List<FollowResponse> followingResponse = page.getContent().stream()
					.map(follow -> new FollowResponse(follow.getId().getFollowingId())).toList();
			pageResponse = new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(),
					followingResponse);
		}
		return pageResponse;
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

}
