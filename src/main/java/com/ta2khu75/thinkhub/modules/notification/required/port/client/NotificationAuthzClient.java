package com.ta2khu75.thinkhub.modules.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.modules.notification.required.port.NotificationAuthzPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

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
