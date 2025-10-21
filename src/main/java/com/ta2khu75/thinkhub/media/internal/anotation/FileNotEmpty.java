package com.ta2khu75.thinkhub.media.internal.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ta2khu75.thinkhub.media.internal.validator.FileNotEmptyValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileNotEmptyValidator.class)
public @interface FileNotEmpty {
	String message() default "File must not be empty";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
