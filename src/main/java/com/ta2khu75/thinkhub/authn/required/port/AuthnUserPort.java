package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

public interface AuthnUserPort {
//	UserResponse create(UserCreate request);

	UserResponse readByEmail(String email);

	UserSummary create(UserSummary request);

	UserSummary readSummary(Long id);

	UserSummary readSummary(String id);

	UserSummary readSummaryByEmail(String email);
}
