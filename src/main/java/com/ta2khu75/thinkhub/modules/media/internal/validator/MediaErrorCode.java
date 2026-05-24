package com.ta2khu75.thinkhub.modules.media.internal.validator;

import com.ta2khu75.thinkhub.modules.media.internal.domain.Media;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum MediaErrorCode implements ErrorCode {
	NOT_FOUND, INVALID_STATE, INVALID_FILENAME, INVALID_URL, INVALID_SIZE, INVALID_TYPE;

	@Override
	public String getCode() {
		return format(Media.class, this.name());
	}

}