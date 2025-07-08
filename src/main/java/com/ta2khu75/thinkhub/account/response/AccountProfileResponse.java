package com.ta2khu75.thinkhub.account.response;

import java.time.Instant;
import java.time.LocalDate;

public record AccountProfileResponse(Long id, String firstName, String lastName, LocalDate birthday, String displayName,
		Instant updatedAt) {
}
