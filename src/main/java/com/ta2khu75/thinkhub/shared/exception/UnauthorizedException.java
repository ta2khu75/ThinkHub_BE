package com.ta2khu75.thinkhub.shared.exception;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;
import com.ta2khu75.thinkhub.shared.exception.ForbiddenException.ForbiddenErrorCode;

public class UnauthorizedException extends BaseException {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(UnauthorizedErrorCode.NOT_AUTHORIZED, message, 401);
	}

	enum UnauthorizedErrorCode implements ErrorCode {
		NOT_AUTHORIZED;

		@Override
		public String getCode() {
			return ForbiddenErrorCode.class.getSimpleName() + ":" + name();
		}
	}
}
