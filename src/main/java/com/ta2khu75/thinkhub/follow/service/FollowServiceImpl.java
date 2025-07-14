package com.ta2khu75.thinkhub.follow.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.quiz.exception.ExistingException;
import com.ta2khu75.quiz.exception.InvalidDataException;
import com.ta2khu75.quiz.mapper.FollowMapper;
import com.ta2khu75.quiz.model.entity.AccountProfile;
import com.ta2khu75.quiz.model.entity.Follow;
import com.ta2khu75.quiz.model.entity.id.FollowId;
import com.ta2khu75.quiz.model.response.FollowResponse;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.repository.FollowRepository;
import com.ta2khu75.quiz.repository.account.AccountProfileRepository;
import com.ta2khu75.quiz.service.FollowService;
import com.ta2khu75.quiz.service.base.BaseService;
import com.ta2khu75.quiz.util.FunctionUtil;
import com.ta2khu75.quiz.util.SecurityUtil;

@Service
public class FollowServiceImpl extends BaseService<FollowRepository, FollowMapper> implements FollowService {
	private final AccountProfileRepository profileRepository;

	public FollowServiceImpl(FollowRepository repository, AccountProfileRepository profileRepository, FollowMapper mapper) {
		super(repository, mapper);
		this.profileRepository=profileRepository;
	}

	@Override
	@Transactional
	public FollowResponse create(Long followingId) {
		 AccountProfile follower = SecurityUtil.getCurrentProfile();
		if(followingId.equals(follower.getId())) {
			throw new InvalidDataException("Cannot follow yourself");
		}
		Optional<Follow> existingFollow = repository.findById(new FollowId(1L, followingId));
		if (existingFollow.isPresent()) {
			throw new ExistingException("Already following this user");
		}
		AccountProfile following = FunctionUtil.findOrThrow(followingId, AccountProfile.class, profileRepository::findById);
		Follow follow = new Follow();
		follow.setId(new FollowId(follower.getId(), following.getId()));
		follow.setFollower(SecurityUtil.getCurrentProfile());
		follow.setFollowing(following);
		return mapper.toResponse(repository.save(follow));
	}

	@Override
	@Transactional
	public void delete(Long followingId) {
		Long followerId = SecurityUtil.getCurrentProfileId();
		repository.deleteById(new FollowId(followerId, followingId));
	}

	@Override
	public FollowResponse read(Long followingId) {
		Long followerId = SecurityUtil.getCurrentProfileId();
		Follow follow = repository.findById(new FollowId(followerId, followingId)).orElse(null);
		return follow == null ? null : mapper.toResponse(follow);
	}

	@Override
	public PageResponse<FollowResponse> readPage(Long followingId, Pageable pageable) {
		return mapper.toPageResponse(repository.findByFollowingId(1L, pageable));
	}

}
