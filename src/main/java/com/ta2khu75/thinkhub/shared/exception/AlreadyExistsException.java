package com.ta2khu75.thinkhub.shared.exception;

public class AlreadyExistsException extends BaseException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(String message) {
		super(message, 409);
	}
}
