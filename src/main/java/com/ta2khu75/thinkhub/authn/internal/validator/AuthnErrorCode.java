package com.ta2khu75.thinkhub.authn.internal.validator;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum AuthnErrorCode implements ErrorCode {

	PASSWORD_MISMATCH, NEW_PASSWORD_SAME_AS_OLD, CURRENT_PASSWORD_INVALID, EMAIL_ALREADY_EXISTS, INVALID_CREDENTIALS,
	ACCOUNT_LOCKED;

	@Override
	public String getCode() {
		return "AUTH:" + name();
	}

}
