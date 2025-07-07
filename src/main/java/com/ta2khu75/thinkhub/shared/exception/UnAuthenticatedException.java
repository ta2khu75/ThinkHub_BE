package com.ta2khu75.thinkhub.shared.exception;

public class UnAuthenticatedException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public UnAuthenticatedException(String message) {
		super(message);
	}
}