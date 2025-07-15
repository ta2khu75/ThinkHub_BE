package com.ta2khu75.thinkhub.notification.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.follow.FollowService;
import com.ta2khu75.thinkhub.follow.entity.FollowId;
import com.ta2khu75.thinkhub.follow.event.FollowEvent;
import com.ta2khu75.thinkhub.follow.event.UnFollowEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationListener {
	private final FollowService service;

	@ApplicationModuleListener
	public void onFollow(FollowEvent event) {
		service.follow(new FollowId(event.followingId(), event.followerId()));
	}

	@ApplicationModuleListener
	public void handleUnFollow(UnFollowEvent event) {
		service.unFollow(new FollowId(event.followingId(), event.followerId()));
	}
}
