package com.ta2khu75.thinkhub.shared.exception;

public class ForbiddenException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(message,403);
	}
}