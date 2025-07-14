package com.ta2khu75.thinkhub.shared.exception;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final int status;

	public BaseException(String message, int status) {
		super(message);
		this.status = status;
	}

	public int getStatusCode() {
		return status;
	}
}
