package com.ta2khu75.thinkhub.account;

import java.time.Instant;

import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.dto.BaseResponse;

public record AccountDto(String id, Instant createdAt, Instant updatedAt, String createdBy, String email,
		String password, AccountProfileResponse profile, AccountStatusResponse status) implements BaseResponse<String> {
}
