package com.ta2khu75.thinkhub.modules.authn.required.port;

import com.ta2khu75.thinkhub.modules.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;

public interface AuthnAuthzPort {
	RoleResponse readByName(String name);

	RoleSummary readSummary(Long id);

	RoleSummary readSummaryByName(String name);

}
