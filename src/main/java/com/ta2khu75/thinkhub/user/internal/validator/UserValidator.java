package com.ta2khu75.thinkhub.user.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.shared.exception.AbstractBizValidator;
import com.ta2khu75.thinkhub.shared.exception.ForbiddenException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.internal.entity.UserStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator extends AbstractBizValidator {

	public void validateCreate(UserCreateRequest request, boolean emailExists) {
		ensure(!emailExists, UserErrorCode.EMAIL_EXISTS, "Email already exists");
	}

	public void validateUpdateStatus(UserStatus status, UserStatusRequest request, Long targetUserId) {
		if (status == null) {
			throw new NotFoundException(UserErrorCode.STATUS_NOT_FOUND, "Could not find user status");
		}
		// Không cho tự khoá chính mình
		if (SecurityUtil.getCurrentUserIdDecode().equals(targetUserId) && !request.nonLocked()) {
			throw new ForbiddenException(UserErrorCode.CANNOT_LOCK_SELF, "You cannot lock yourself");
		}
	}

//	public void validateRoleChange(UserStatus status, Long newRoleId) {
//		if (!status.getRoleId().equals(newRoleId)) {
//			// chỉ validate, KHÔNG publish event
//		}
//	}

	public boolean isRoleChanged(UserStatus status, UserStatusRequest request) {
		return !status.getRoleId().equals(request.roleId());
	}
}
