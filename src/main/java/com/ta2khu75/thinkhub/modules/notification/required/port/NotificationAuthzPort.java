package com.ta2khu75.thinkhub.modules.notification.required.port;

import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;

public interface NotificationAuthzPort {
	RoleResponse readRoleByName(String name);
}
