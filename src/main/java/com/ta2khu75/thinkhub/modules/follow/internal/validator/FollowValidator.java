package com.ta2khu75.thinkhub.modules.follow.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

@Component
public class FollowValidator extends BaseValidator {
	public void validateFollow(Long followerId, Long followingId, boolean alreadyFollowed) {

		// Business rule: không được follow chính mình
		ensure(!followerId.equals(followingId), FollowErrorCode.CANNOT_FOLLOW_SELF, "Cannot follow yourself");

		// Business state conflict
		conflict(alreadyFollowed, FollowErrorCode.ALREADY_FOLLOWED, "Already followed this user");
	}
}
