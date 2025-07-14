package com.ta2khu75.thinkhub.shared.exception;

public class ReturnException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Object data;

	public ReturnException(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}
}
