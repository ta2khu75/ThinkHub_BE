package com.ta2khu75.thinkhub.user.api.dto;

import java.time.Instant;
import java.time.LocalDate;

public record UserResponse(String id, String firstName, String lastName, String username, LocalDate birthday,
		Instant createdAt, Instant updatedAt, String createdBy) {
}
