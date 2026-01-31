package com.ta2khu75.thinkhub.follow.internal.validator;

import com.ta2khu75.thinkhub.follow.internal.entity.Follow;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum FollowErrorCode implements ErrorCode {
	CANNOT_FOLLOW_SELF, ALREADY_FOLLOWED;

	@Override
	public String getCode() {
		return Follow.class.getSimpleName() + ":" + this.name();
	}
}
