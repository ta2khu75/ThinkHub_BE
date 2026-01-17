package com.ta2khu75.thinkhub.shared.exception;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public class NotFoundException extends BaseException {
	private static final long serialVersionUID = 1L;

	public NotFoundException(ErrorCode code, String message) {
		super(code, message, 404);
	}
	public NotFoundException(String code, String message) {
		super(code, message, 404);
	}
}
