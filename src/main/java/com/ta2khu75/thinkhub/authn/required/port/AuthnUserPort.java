package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

public interface AuthnUserPort {
	UserSummary create(UserCreateRequest request);

	UserSummary readSummary(String id);

	UserSummary readSummaryByEmail(String email);
}
