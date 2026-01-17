package com.ta2khu75.thinkhub.shared.exception;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public class ForbiddenException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(ForbiddenErrorCode.NO_PERMISSION, message, 403);
	}

	public ForbiddenException(ErrorCode code, String message) {
		super(code, message, 403);
	}

	enum ForbiddenErrorCode implements ErrorCode {
		NO_PERMISSION;

		@Override
		public String getCode() {
			return ForbiddenErrorCode.class.getSimpleName() + ":" + name();
		}
	}
}