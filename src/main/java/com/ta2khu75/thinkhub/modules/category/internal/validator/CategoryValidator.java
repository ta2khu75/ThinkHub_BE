package com.ta2khu75.thinkhub.modules.category.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.category.internal.domain.Category;
import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

@Component
public class CategoryValidator extends BaseValidator {
	public void validateExists(Category category, Long id) {
		notFound(category, CategoryErrorCode.NOT_FOUND, "Could not find category with id " + id);
	}
}
