package com.ta2khu75.thinkhub.authn.internal.validator;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthnValidator extends BaseValidator {
	/* ================= REGISTER ================= */

	public void validateRegister(RegisterRequest request) {
		ensurePasswordsMatch(request.password(), request.confirmPassword());
	}

	/* ================= CHANGE PASSWORD ================= */

	public void validateChangePassword(ChangePasswordRequest request, AuthProviderSummary authProvider,
			PasswordEncoder encoder) {

		ensurePasswordsMatch(request.newPassword(), request.confirmPassword());

		ensure(!request.newPassword().equals(request.password()), AuthnErrorCode.NEW_PASSWORD_SAME_AS_OLD,
				"New password must be different from current password");

		ensure(encoder.matches(request.password(), authProvider.password()),
				AuthnErrorCode.CURRENT_PASSWORD_INVALID, "Current password is incorrect");
	}

	/* ================= ATOMIC RULES ================= */
	private void ensurePasswordsMatch(String password, String confirmPassword) {
		ensure(Objects.equals(password, confirmPassword), AuthnErrorCode.PASSWORD_MISMATCH,
				"Password and confirm password do not match");
	}
}
