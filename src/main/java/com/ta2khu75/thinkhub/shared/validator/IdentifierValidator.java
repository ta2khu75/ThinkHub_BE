package com.ta2khu75.thinkhub.shared.validator;

import java.util.regex.Pattern;

import com.ta2khu75.thinkhub.shared.anotation.ValidIdentifier;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdentifierValidator implements ConstraintValidator<ValidIdentifier, String> {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

	private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,30}$" // ví dụ: username không có
																							// dấu, 4-30 ký tự
	);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank())
			return false;

		if (value.contains("@")) {
			return EMAIL_PATTERN.matcher(value).matches();
		} else {
			return USERNAME_PATTERN.matcher(value).matches();
		}
	}
}
