package com.ta2khu75.thinkhub.shared.exception;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public class BusinessException extends BaseException {
	private static final long serialVersionUID = 1L;

	public BusinessException(ErrorCode code, String message) {
		super(code, message, 422);
	}
}
