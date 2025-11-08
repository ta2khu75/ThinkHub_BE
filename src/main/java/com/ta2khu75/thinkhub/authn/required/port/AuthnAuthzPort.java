package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;

public interface AuthnAuthzPort {
	RoleResponse readByName(String name);

	RoleSummary readSummary(Long id);

	RoleSummary readSummaryByName(String name);

}
