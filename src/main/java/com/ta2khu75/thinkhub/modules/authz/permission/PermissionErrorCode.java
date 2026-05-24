package com.ta2khu75.thinkhub.modules.authz.permission;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum PermissionErrorCode implements ErrorCode {
	NOT_FOUND;

	@Override
	public String getCode() {
		return Permission.class.getSimpleName() + ":" + name();
	}

}
