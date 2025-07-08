package com.ta2khu75.thinkhub.account.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountRequest(
		@NotBlank(message = "Email must not be blank") @Email(message = "Email should be a valid email address") String email,

		@NotBlank(message = "Password must not be blank") String password,

		@NotBlank(message = "Confirm password must not be blank") String confirmPassword,

		@Valid AccountProfileRequest profile, @Valid AccountStatusRequest status) {
}
