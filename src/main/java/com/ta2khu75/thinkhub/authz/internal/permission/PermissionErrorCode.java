package com.ta2khu75.thinkhub.authz.internal.permission;

import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public enum PermissionErrorCode implements ErrorCode {
	NOT_FOUND;

	@Override
	public String getCode() {
		return Permission.class.getSimpleName() + ":" + name();
	}

}
