package com.ta2khu75.thinkhub.notification.required.port;

import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;

public interface NotificationAuthzPort {
	RoleResponse readRoleByName(String name);
}
