package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;

public interface AuthnAuthzPort {
	RoleDto readDto(Long id);
	RoleResponse readByName(String name);
}
