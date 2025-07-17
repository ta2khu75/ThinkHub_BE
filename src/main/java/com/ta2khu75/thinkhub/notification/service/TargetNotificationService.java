package com.ta2khu75.thinkhub.notification.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.follow.FollowDirection;
import com.ta2khu75.thinkhub.follow.FollowService;
import com.ta2khu75.thinkhub.follow.dto.FollowResponse;
import com.ta2khu75.thinkhub.notification.NotificationService;
import com.ta2khu75.thinkhub.notification.dto.NotificationRequest;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TargetNotificationService {
	private final FollowService followService;
	private final NotificationService notificationService;

	public void notifyFollowersAboutNewTarget(NotificationRequest request) {
		Search search = new Search();
		search.setPage(0);
		search.setSize(1000);
		PageResponse<FollowResponse> followerPage = followService.readPage(request.getAccountId(),
				FollowDirection.FOLLOWER, search);
		int totalPage = followerPage.getTotalPages();
		NotificationRequest notificationRequest = new NotificationRequest();
		BeanUtils.copyProperties(request, notificationRequest);
		do {
			for (FollowResponse follower : followerPage.getContent()) {
				notificationRequest.setAccountId(follower.id());
				notificationService.create(notificationRequest);
			}
			search.setPage(followerPage.getPage() + 1);
			followerPage = followService.readPage(request.getAccountId(), FollowDirection.FOLLOWER, search);
		} while (followerPage.getPage() < totalPage);
	}
}
