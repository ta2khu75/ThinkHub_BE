package com.ta2khu75.thinkhub.shared.exception;

public class MismatchException extends BaseException {
	private static final long serialVersionUID = 1L;

	public MismatchException(String message) {
		super(message, 400);
	}
}
