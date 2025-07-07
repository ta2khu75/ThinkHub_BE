package com.ta2khu75.thinkhub.account.request;

import com.ta2khu75.thinkhub.shared.group.Admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
		@NotBlank(message = "Email must not be blank") @Email(message = "Email should be a valid email address") String email,

		@NotBlank(message = "Password must not be blank") String password,

		@NotBlank(message = "Confirm password must not be blank") String confirmPassword,

		@NotNull(message = "Role id must not be null", groups = Admin.class) Long roleId,
		@Valid AccountProfileRequest profile) {
}
