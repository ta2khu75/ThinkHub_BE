package com.ta2khu75.thinkhub.shared.domain.enums;

public interface ErrorCode {
	String getCode();

	default String format(Class<?> clazz, String error) {
		return "%s:%s".formatted(clazz.getSimpleName(), error);
	}
}
