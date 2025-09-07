package com.ta2khu75.thinkhub.authentication.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message = "Identifier must not be blank") @Email(message = "Email should be a valid email address") String email,
		@NotBlank(message = "Password must not be blank") String password) {
}
