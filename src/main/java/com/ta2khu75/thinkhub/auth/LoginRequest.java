package com.ta2khu75.thinkhub.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message = "Email must not be blank") @Email(message = "Email should be a valid email address") String email,
		@NotBlank(message = "Password must not be blank") String password) {
}
