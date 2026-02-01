package com.ta2khu75.thinkhub.modules.notification.required.port.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.notification.required.port.NotificationUserPort;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
public class NotificationUserClient extends BaseClient<UserApi> implements NotificationUserPort {

	protected NotificationUserClient(UserApi api) {
		super(api);
	}

	@Override
	public List<Long> readAllUserIdByRoleId(Long id) {
		return api.readAllUserIdByRoleId(id);
	}

}
