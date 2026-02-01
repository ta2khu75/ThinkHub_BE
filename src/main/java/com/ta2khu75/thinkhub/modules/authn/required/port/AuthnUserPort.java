package com.ta2khu75.thinkhub.modules.authn.required.port;

import com.ta2khu75.thinkhub.modules.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSummary;

public interface AuthnUserPort {
	UserSummary create(UserCreateRequest request);

	UserSummary readSummary(String id);

	UserSummary readSummaryByEmail(String email);
}
