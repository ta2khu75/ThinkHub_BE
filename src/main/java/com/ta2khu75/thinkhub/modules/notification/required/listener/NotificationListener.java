package com.ta2khu75.thinkhub.modules.notification.required.listener;

import java.util.List;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.modules.comment.api.event.CommentCreatedEvent;
import com.ta2khu75.thinkhub.modules.follow.api.event.FollowTargetCreatedEvent;
import com.ta2khu75.thinkhub.modules.notification.api.NotificationApi;
import com.ta2khu75.thinkhub.modules.notification.api.NotificationTargetType;
import com.ta2khu75.thinkhub.modules.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.modules.notification.required.port.NotificationAuthzPort;
import com.ta2khu75.thinkhub.modules.notification.required.port.NotificationUserPort;
import com.ta2khu75.thinkhub.modules.report.api.event.ReportCreatedEvent;
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
