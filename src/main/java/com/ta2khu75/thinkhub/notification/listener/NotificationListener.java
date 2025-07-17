package com.ta2khu75.thinkhub.notification.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.comment.CommentCreatedEvent;
import com.ta2khu75.thinkhub.notification.NotificationTargetType;
import com.ta2khu75.thinkhub.notification.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.service.TargetNotificationService;
import com.ta2khu75.thinkhub.post.PostCreatedEvent;
import com.ta2khu75.thinkhub.quiz.QuizCreatedEvent;

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
