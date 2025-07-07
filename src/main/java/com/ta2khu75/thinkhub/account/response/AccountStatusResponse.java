package com.ta2khu75.thinkhub.account.response;

import java.time.Instant;

public record AccountStatusResponse(Long id, Boolean enabled, Boolean nonLocked, Long roleId, Instant updatedAt, String updatedBy) {
}