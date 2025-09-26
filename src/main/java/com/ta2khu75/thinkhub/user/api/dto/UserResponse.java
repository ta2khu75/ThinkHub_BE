package com.ta2khu75.thinkhub.user.api.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.ta2khu75.thinkhub.shared.api.dto.BaseRecordResponse;

public record UserResponse (String id, String firstName, String lastName, String username, LocalDate birthday, String summary,
		Instant createdAt, Instant updatedAt, String createdBy) implements BaseRecordResponse<String> {
}
