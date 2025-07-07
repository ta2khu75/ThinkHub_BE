package com.ta2khu75.thinkhub.account.response;

import java.time.Instant;

import com.ta2khu75.thinkhub.shared.dto.BaseResponse;

public record AccountResponse(String id, Instant createdAt, Instant updatedAt, String createdBy,
		AccountProfileResponse profile, AccountStatusResponse status) implements BaseResponse<String> {
}
