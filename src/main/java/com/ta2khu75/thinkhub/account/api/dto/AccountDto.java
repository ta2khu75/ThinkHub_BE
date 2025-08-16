package com.ta2khu75.thinkhub.account.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.account.api.dto.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.api.dto.BaseRecordResponse;

public record AccountDto(String id, Instant createdAt, Instant updatedAt, String createdBy, String username,
		String password, AccountProfileResponse profile, AccountStatusResponse status) implements BaseRecordResponse<String> {
}
