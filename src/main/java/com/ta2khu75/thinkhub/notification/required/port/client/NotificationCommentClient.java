package com.ta2khu75.thinkhub.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.comment.api.CommentApi;
import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.notification.required.port.NotificationCommentPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
class NotificationCommentClient extends BaseClient<CommentApi> implements NotificationCommentPort {

	protected NotificationCommentClient(CommentApi api) {
		super(api);
	}

	@Override
	public CommentResponse read(Long id) {
		return api.read(id);
	}

}
