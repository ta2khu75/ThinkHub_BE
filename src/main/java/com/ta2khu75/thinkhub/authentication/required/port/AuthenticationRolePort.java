package com.ta2khu75.thinkhub.authentication.required.port;

import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;

public interface AuthenticationRolePort {
	RoleDto readDto(Long id);
	RoleResponse readByName(String name);
}
