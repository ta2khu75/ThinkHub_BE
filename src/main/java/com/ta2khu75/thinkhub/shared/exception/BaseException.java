package com.ta2khu75.thinkhub.shared.exception;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public abstract class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String code;
	private final int status;

	protected BaseException(ErrorCode code, String message, int status) {
		super(message);
		this.status = status;
		this.code = code.getCode();
	}

	protected BaseException(String code, String message, int status) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public int getStatusCode() {
		return status;
	}

	public String getCode() {
		return code;
	}
}
