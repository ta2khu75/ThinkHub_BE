package com.ta2khu75.thinkhub.modules.authProvider.internal.validator;

import com.ta2khu75.thinkhub.modules.authProvider.internal.domain.AuthProvider;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum AuthProviderErrorCode implements ErrorCode {
	NOT_FOUND;

	@Override
	public String getCode() {
		return AuthProvider.class.getSimpleName() + ":" + name();
	}

}
