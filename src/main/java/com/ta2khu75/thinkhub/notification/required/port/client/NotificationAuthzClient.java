package com.ta2khu75.thinkhub.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.notification.required.port.NotificationAuthzPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;

@Component
class NotificationAuthzClient extends BaseClient<AuthzApi> implements NotificationAuthzPort {

	protected NotificationAuthzClient(AuthzApi api) {
		super(api);
	}

	@Override
	public RoleResponse readRoleByName(String name) {
		return api.readRoleByName(name);
	}

}
