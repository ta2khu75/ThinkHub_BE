package com.ta2khu75.thinkhub.account.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.account.dto.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.dto.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.dto.BaseRecordResponse;

public record AccountDto(String id, Instant createdAt, Instant updatedAt, String createdBy, String username,
		String password, AccountProfileResponse profile, AccountStatusResponse status) implements BaseRecordResponse<String> {
}
