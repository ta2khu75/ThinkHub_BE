package com.ta2khu75.thinkhub.notification.required.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.comment.api.event.CommentCreatedEvent;
import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.internal.service.TargetNotificationService;
import com.ta2khu75.thinkhub.post.api.event.PostCreatedEvent;
import com.ta2khu75.thinkhub.quiz.api.event.QuizCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationListener {
	private final TargetNotificationService service;

	@ApplicationModuleListener
	public void onPostCreated(PostCreatedEvent event) {
		service.notifyFollowersAboutNewTarget(
				new NotificationRequest(event.accountId(), event.targetId(), NotificationTargetType.POST));
	}

	@ApplicationModuleListener
	public void onQuizCreated(QuizCreatedEvent event) {
		service.notifyFollowersAboutNewTarget(
				new NotificationRequest(event.accountId(), event.targetId(), NotificationTargetType.QUIZ));
	}

	@ApplicationModuleListener
	public void onCommentCreated(CommentCreatedEvent event) {
		service.notifyFollowersAboutNewTarget(
				new NotificationRequest(event.accountId(), event.targetId(), NotificationTargetType.COMMENT));
	}
}
