package com.ta2khu75.thinkhub.modules.report.internal.validator;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum ReportErrorCode implements ErrorCode {
	NOT_FOUND, UPDATE_NOT_ALLOWED, NOT_AUTHOR;

	@Override
	public String getCode() {
		return ReportErrorCode.class.getSimpleName() + ":" + name();
	}

}
