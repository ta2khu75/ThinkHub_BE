package com.ta2khu75.thinkhub.user.internal.validator;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;
import com.ta2khu75.thinkhub.user.internal.entity.User;

public enum UserErrorCode implements ErrorCode {
	EMAIL_EXISTS, NOT_FOUND, UPDATE_FORBIDDEN, STATUS_NOT_FOUND, CANNOT_LOCK_SELF;

	@Override
	public String getCode() {
		return User.class.getSimpleName() + ":" + name();
	}
}
