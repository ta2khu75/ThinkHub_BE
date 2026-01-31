package com.ta2khu75.thinkhub.authz.internal.role;

import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum RoleErrorCode implements ErrorCode {
	NOT_FOUND;

	@Override
	public String getCode() {
		return Role.class.getSimpleName() + ":" + name();
	}

}
