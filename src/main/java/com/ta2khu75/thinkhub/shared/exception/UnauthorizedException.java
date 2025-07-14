package com.ta2khu75.thinkhub.shared.exception;

public class UnauthorizedException extends BaseException {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(message, 401);
	}
}
