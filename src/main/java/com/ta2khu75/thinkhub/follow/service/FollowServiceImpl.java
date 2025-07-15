package com.ta2khu75.thinkhub.follow.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.follow.FollowDirection;
import com.ta2khu75.thinkhub.follow.FollowService;
import com.ta2khu75.thinkhub.follow.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.follow.entity.Follow;
import com.ta2khu75.thinkhub.follow.entity.FollowId;
import com.ta2khu75.thinkhub.follow.repository.FollowRepository;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
	private final FollowRepository repository;
	private final ApplicationEventPublisher events;

	@Override
	@Transactional
	public void follow(Long followingId) {
		Long followerId = SecurityUtil.getCurrentAccountIdDecode();
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
	public void unFollow(Long followingId) {
		Long followerId = SecurityUtil.getCurrentAccountIdDecode();
		repository.deleteById(new FollowId(followingId, followerId));
	}

	@Override
	public PageResponse<FollowResponse> readPage(Long followingId, FollowDirection direction, Search search) {
		Pageable pageable = search.toPageable();
		if (direction == FollowDirection.FOLLOWING) {
			Page<Follow> page = repository.findByFollowingId(followingId, pageable);
			List<FollowResponse> followerResponse = page.getContent().stream()
					.map(follow -> new FollowResponse(follow.getId().getFollowerId())).toList();
			return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(),
					followerResponse);
		} else {
			Page<Follow> page = repository.findByFollowerId(followingId, pageable);
			List<FollowResponse> followingResponse = page.getContent().stream()
					.map(follow -> new FollowResponse(follow.getId().getFollowingId())).toList();
			return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(),
					followingResponse);
		}
	}

	@Override
	public FollowStatusResponse isFollowing(Long followingId) {
		Long followerId = SecurityUtil.getCurrentAccountIdDecode();
		return new FollowStatusResponse(repository.existsById(new FollowId(followingId, followerId)));
	}

}
