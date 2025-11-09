package com.ta2khu75.thinkhub.user.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(@NotBlank @Email String email, @NotBlank String firstName, @NotBlank String lastName,
		String username, @Valid UserStatusRequest status) {
}
