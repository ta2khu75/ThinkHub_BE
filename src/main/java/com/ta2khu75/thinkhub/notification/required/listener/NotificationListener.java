package com.ta2khu75.thinkhub.notification.required.listener;

import java.util.List;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.comment.api.event.CommentCreatedEvent;
import com.ta2khu75.thinkhub.follow.api.event.FollowTargetCreatedEvent;
import com.ta2khu75.thinkhub.notification.api.NotificationApi;
import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.required.port.NotificationAuthzPort;
import com.ta2khu75.thinkhub.notification.required.port.NotificationUserPort;
import com.ta2khu75.thinkhub.report.api.event.ReportCreatedEvent;
import com.ta2khu75.thinkhub.shared.domain.enums.RoleDefault;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationListener {
	private final NotificationApi api;
	private final NotificationAuthzPort authzPort;
	private final NotificationUserPort userPort;

	@ApplicationModuleListener
	public void onFollowTargetCreated(FollowTargetCreatedEvent request) {
		api.create(new NotificationRequest(request.userId(), request.targetId(), request.targetType()));
	}

	@ApplicationModuleListener
	public void onCommentCreated(CommentCreatedEvent event) {
		api.create(new NotificationRequest(event.userId(), event.targetId(), NotificationTargetType.COMMENT));
	}

	@ApplicationModuleListener
	public void onReportCreated(ReportCreatedEvent event) {
		RoleResponse role = authzPort.readRoleByName(RoleDefault.ADMIN.name());
		List<Long> userIds = userPort.readAllUserIdByRoleId(role.id());
		userIds.forEach(
				userId -> api.create(new NotificationRequest(userId, event.id(), NotificationTargetType.REPORT)));
	}
}
