package com.ta2khu75.thinkhub.authn.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;

@Component
public class AuthnAuthzClient extends BaseClient<AuthzApi> implements AuthnAuthzPort {

	protected AuthnAuthzClient(AuthzApi api) {
		super(api);
	}

	@Override
	public RoleDto readDto(Long id) {
		return api.readRoleDto(id);
	}

	@Override
	public RoleResponse readByName(String name) {
		return api.readRoleByName(name);
	}

}
