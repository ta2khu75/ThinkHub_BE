package com.ta2khu75.thinkhub.shared.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ta2khu75.thinkhub.shared.validator.IdentifierValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = IdentifierValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentifier {
	String message() default "Identifier must be a valid email or username";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
