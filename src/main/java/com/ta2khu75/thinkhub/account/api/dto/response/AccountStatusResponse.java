package com.ta2khu75.thinkhub.account.api.dto.response;

import java.time.Instant;

public record AccountStatusResponse(Long id, Boolean enabled, Boolean nonLocked, Long roleId, Instant updatedAt, String updatedBy) {
}