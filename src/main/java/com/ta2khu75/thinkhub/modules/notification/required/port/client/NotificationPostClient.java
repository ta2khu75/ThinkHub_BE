package com.ta2khu75.thinkhub.modules.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.notification.required.port.NotificationPostPort;
import com.ta2khu75.thinkhub.modules.post.api.PostApi;
import com.ta2khu75.thinkhub.modules.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
class NotificationPostClient extends BaseClient<PostApi> implements NotificationPostPort {

	protected NotificationPostClient(PostApi api) {
		super(api);
	}

	@Override
	public PostResponse read(Long id) {
		return api.read(id);
	}

}
