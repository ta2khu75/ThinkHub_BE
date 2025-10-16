package com.ta2khu75.thinkhub.user.api.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank(message = "First name must not be blank") String firstName,
		@NotBlank(message = "Last name must not be blank") String lastName,
		@NotBlank(message = "Email must not be blank") @Email(message = "Email should be a valid email address") String email,
		LocalDate birthday, String summary, @Valid UserStatusRequest status) {
}