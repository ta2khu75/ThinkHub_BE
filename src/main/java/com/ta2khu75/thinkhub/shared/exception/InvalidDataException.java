package com.ta2khu75.thinkhub.shared.exception;

public class InvalidDataException extends BaseException {
    private static final long serialVersionUID = 1L;

	public InvalidDataException(String message) {
        super(message, 400);
    }
}
