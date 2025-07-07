package com.ta2khu75.thinkhub.account.response;

import java.time.Instant;

public record AccountProfileResponse(Long id, String firstName, String lastName, Instant birthday, String displayName,
		Integer blogCount, Integer quizCount, Integer followCount, Instant updatedAt) {
}
