package com.ta2khu75.thinkhub.user.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserSummary(String id, @NotBlank String firstName, @NotBlank String lastName,
		@NotBlank @Email String email, String username, @Valid UserStatusSummary status) {
}
