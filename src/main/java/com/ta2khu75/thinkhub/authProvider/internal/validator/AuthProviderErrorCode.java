package com.ta2khu75.thinkhub.authProvider.internal.validator;

import com.ta2khu75.thinkhub.authProvider.internal.entity.AuthProvider;
import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public enum AuthProviderErrorCode implements ErrorCode {
	NOT_FOUND;

	@Override
	public String getCode() {
		return AuthProvider.class.getSimpleName() + ":" + name();
	}

}
