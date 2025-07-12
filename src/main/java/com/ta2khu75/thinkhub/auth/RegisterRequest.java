package com.ta2khu75.thinkhub.auth;

import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank(message = "Username must not be blank") String username,
		@NotBlank(message = "Password must not be blank") String password,
		@NotBlank(message = "Confirm password must not be blank") String confirmPassword,
		@NotBlank(message = "Email must not be blank") @Email(message = "Email should be a valid email address") String email,
		@Valid AccountProfileRequest profile) {

}
