package com.ta2khu75.thinkhub.report.internal.validator;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public enum ReportErrorCode implements ErrorCode {
	NOT_FOUND, UPDATE_NOT_ALLOWED, NOT_AUTHOR;

	@Override
	public String getCode() {
		return ReportErrorCode.class.getSimpleName() + ":" + name();
	}

}
