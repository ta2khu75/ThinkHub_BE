package com.ta2khu75.thinkhub.authentication.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authentication.required.port.AuthenticationRolePort;
import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authorization.internal.role.RoleService;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;

@Component
public class AuthenticationRoleClient extends BaseClient<RoleService> implements AuthenticationRolePort {

	protected AuthenticationRoleClient(RoleService api) {
		super(api);
	}

	@Override
	public RoleDto readDto(Long id) {
		return api.readDto(id);
	}

	@Override
	public RoleResponse readByName(String name) {
		return api.readByName(name);
	}

}
