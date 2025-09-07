package com.ta2khu75.thinkhub.user.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotBlank(message = "First name must not be blank") String firstName,
		@NotBlank(message = "Last name must not be blank") String lastName,
		@NotNull(message = "Birthday must not be null") LocalDate birthday,
		@NotBlank(message = "Display name must not be blank") String username, String summary) {
}
