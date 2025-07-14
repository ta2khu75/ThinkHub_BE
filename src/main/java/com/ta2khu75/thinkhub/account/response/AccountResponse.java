package com.ta2khu75.thinkhub.account.response;

import java.time.Instant;

import com.ta2khu75.thinkhub.shared.dto.BaseRecordResponse;

public record AccountResponse(String id, String username, Instant createdAt, Instant updatedAt, String createdBy,
		AccountProfileResponse profile, AccountStatusResponse status) implements BaseRecordResponse<String> {
}
