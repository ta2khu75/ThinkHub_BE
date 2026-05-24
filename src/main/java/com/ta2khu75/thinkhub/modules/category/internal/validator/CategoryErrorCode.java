package com.ta2khu75.thinkhub.modules.category.internal.validator;

import com.ta2khu75.thinkhub.modules.category.internal.domain.Category;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum CategoryErrorCode implements ErrorCode {
	NOT_FOUND, DELETED, INVALID_NAME, INVALID_SLUG, INVALID_IMAGE, INVALID_STATE;

	@Override
	public String getCode() {
		return Category.class.getSimpleName() + ":" + name();
	}

}
