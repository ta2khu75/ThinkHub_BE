package com.ta2khu75.thinkhub.shared.exception;

public class UnAuthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnAuthorizedException(String message) {
		super(message);
	}
}
