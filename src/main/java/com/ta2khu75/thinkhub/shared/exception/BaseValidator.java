package com.ta2khu75.thinkhub.shared.exception;

import java.util.Collection;
import java.util.Objects;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public abstract class BaseValidator {

	// ---------------- Basic null/empty checks ----------------

	/** Giá trị không được null → BadRequest */
	protected void notNull(Object value, ErrorCode code, String message) {
		if (value == null) {
			throw new BadRequestException(code, message);
		}
	}

	/** String không được null hoặc blank → BadRequest */
	protected void notBlank(String value, ErrorCode code, String message) {
		if (value == null || value.trim().isEmpty()) {
			throw new BadRequestException(code, message);
		}
	}

	/** Collection không được null hoặc empty → BadRequest */
	protected void notEmptyCollection(Collection<?> value, ErrorCode code, String message) {
		if (value == null || value.isEmpty()) {
			throw new BadRequestException(code, message);
		}
	}

	// ---------------- Generic condition checks ----------------

	/** Kiểm tra điều kiện kỹ thuật → BadRequest */
	protected void require(boolean condition, ErrorCode code, String message) {
		if (!condition) {
			throw new BadRequestException(code, message);
		}
	}

	/** Kiểm tra điều kiện business/invariant → BusinessException */
	protected void ensure(boolean condition, ErrorCode code, String message) {
		if (!condition) {
			throw new BusinessException(code, message);
		}
	}

	/** Kiểm tra uniqueness / conflict → ConflictException (HTTP 409) */
	protected void conflict(boolean condition, ErrorCode code, String message) {
		if (condition) {
			throw new ConflictException(code, message);
		}
	}

	/** Kiểm tra resource/entity tồn tại → NotFoundException (HTTP 404) */
	protected void notFound(Object value, ErrorCode code, String message) {
		if (value == null) {
			throw new NotFoundException(code, message);
		}
	}

	// ---------------- Numeric / Range / Cross-field ----------------

	/** Kiểm tra giá trị trong range → BadRequest */
	protected void inRange(int value, int min, int max, ErrorCode code, String message) {
		if (value < min || value > max) {
			throw new BadRequestException(code, message);
		}
	}

	/** Kiểm tra 2 field bằng nhau → BusinessException */
	protected void assertEqual(Object a, Object b, ErrorCode code, String message) {
		if (!Objects.equals(a, b)) {
			throw new BusinessException(code, message);
		}
	}

	/** Kiểm tra pattern (email, phone, UUID…) → BadRequest */
	protected void matchPattern(String value, String regex, ErrorCode code, String message) {
		if (value == null || !value.matches(regex)) {
			throw new BadRequestException(code, message);
		}
	}
}
