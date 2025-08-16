package com.ta2khu75.thinkhub.auth.api.dto;

import com.ta2khu75.thinkhub.shared.anotation.ValidIdentifier;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@ValidIdentifier
		 String identifier,
		@NotBlank(message = "Password must not be blank") String password) {
}
