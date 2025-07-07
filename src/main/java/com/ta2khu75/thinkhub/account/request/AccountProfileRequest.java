package com.ta2khu75.thinkhub.account.request;

import java.time.Instant;

import com.ta2khu75.thinkhub.shared.group.Update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountProfileRequest(@NotBlank(message = "First name must not be blank") String firstName,
		@NotBlank(message = "Last name must not be blank") String lastName,
		@NotNull(message = "Birthday must not be null") Instant birthday,
		@NotBlank(message = "Display name must not be blank", groups = Update.class) String displayName) {
}
