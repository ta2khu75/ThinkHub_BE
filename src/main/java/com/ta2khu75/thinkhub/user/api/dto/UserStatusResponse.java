package com.ta2khu75.thinkhub.user.api.dto;

import java.time.Instant;


public record UserStatusResponse(Long id, Boolean enabled, Boolean nonLocked, Long roleId, Instant updatedAt,
		String updatedBy) {
}