package com.ta2khu75.thinkhub.follow.required.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.follow.api.FollowApi;
import com.ta2khu75.thinkhub.follow.api.FollowDirection;
import com.ta2khu75.thinkhub.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.api.event.FollowTargetCreatedEvent;
import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;
import com.ta2khu75.thinkhub.post.api.event.PostCreatedEvent;
import com.ta2khu75.thinkhub.quiz.api.event.QuizCreatedEvent;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class FollowListener {
	private final ApplicationEventPublisher events;
	private final FollowApi followApi;

	@ApplicationModuleListener
	void onPostCreated(PostCreatedEvent event) {
		onCreateNotification(event.userId(), event.targetId(), NotificationTargetType.POST);
	}

	@ApplicationModuleListener
	void onQuizCreated(QuizCreatedEvent event) {
		onCreateNotification(event.userId(), event.targetId(), NotificationTargetType.POST);
	}

	private void onCreateNotification(Long userId, Long targetId, NotificationTargetType targetType) {
		Search search = new Search();
		search.setPage(0);
		search.setSize(1000);
		PageResponse<FollowResponse> pageResponse = followApi.readPage(userId, FollowDirection.FOLLOWER, search);
		for (int i = 0; i < pageResponse.getTotalPages(); i++) {
			pageResponse.getContent().forEach(followResponse -> events
					.publishEvent(new FollowTargetCreatedEvent(followResponse.id(), targetId, targetType)));
		}
	}
}
