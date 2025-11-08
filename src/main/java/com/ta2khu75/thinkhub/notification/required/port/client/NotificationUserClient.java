package com.ta2khu75.thinkhub.notification.required.port.client;

import java.util.List;

import com.ta2khu75.thinkhub.notification.required.port.NotificationUserPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.user.api.UserApi;

public class NotificationUserClient extends BaseClient<UserApi> implements NotificationUserPort {

	protected NotificationUserClient(UserApi api) {
		super(api);
	}

	@Override
	public List<Long> readAllUserIdByRoleId(Long id) {
		return api.readAllUserIdByRoleId(id);
	}

}
