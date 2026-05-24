package com.ta2khu75.thinkhub.shared.util;

import java.util.Collection;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;
import com.ta2khu75.thinkhub.shared.exception.BusinessException;

public final class Guard {

	private Guard() {
	}

	/*
	 * ========================= Null / Blank =========================
	 */

	public static <T> T notNull(T value, ErrorCode code, String fieldName) {
		if (value == null) {
			throw new BusinessException(code, fieldName + " must not be null");
		}
		return value;
	}

	public static String notBlank(String value, ErrorCode code, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new BusinessException(code, fieldName + " must not be blank");
		}
		return value;
	}

	public static void maxLength(String value, int max, ErrorCode errorCode, String field) {
		if (value != null && value.length() > max) {
			throw new BusinessException(errorCode, field + " must not exceed " + max + " characters");
		}
	}

	/*
	 * ========================= Number validation =========================
	 */

	public static Long positive(Long value, ErrorCode code, String fieldName) {
		if (value == null || value <= 0) {
			throw new BusinessException(code, fieldName + " must be positive");
		}
		return value;
	}

	public static Integer positive(Integer value, ErrorCode code, String fieldName) {
		if (value == null || value <= 0) {
			throw new BusinessException(code, fieldName + " must be positive");
		}
		return value;
	}

	public static Integer nonNegative(Integer value, ErrorCode code, String field) {
		if (value != null && value < 0) {
			throw new BusinessException(code, field + " must be >= 0");
		}
		return value;
	}

	public static Long nonNegative(Long value, ErrorCode code, String field) {
		if (value != null && value < 0) {
			throw new BusinessException(code, field + " must be >= 0");
		}
		return value;
	}

	public static Integer min(Integer value, int min, ErrorCode code, String field) {
		if (value == null || value < min) {
			throw new BusinessException(code, field + " must be >= " + min);
		}
		return value;
	}

	public static void minIfPresent(Integer value, int min, ErrorCode code, String field) {
		if (value != null && value < min) {
			throw new BusinessException(code, field + " must be >= " + min);
		}
	}
	/*
	 * ========================= State / Condition =========================
	 */

	public static void state(boolean expression, ErrorCode code, String message) {
		if (!expression) {
			throw new BusinessException(code, message);
		}
	}

	public static void condition(boolean expression, ErrorCode code, String message) {
		if (!expression) {
			throw new BusinessException(code, message);
		}
	}

	/*
	 * ========================= Collection =========================
	 */

	public static <T> Collection<T> notEmpty(Collection<T> collection, ErrorCode code, String fieldName) {

		if (collection == null || collection.isEmpty()) {
			throw new BusinessException(code, fieldName + " must not be empty");
		}
		return collection;
	}
}