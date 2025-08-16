package com.ta2khu75.thinkhub.notification.internal.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.follow.api.FollowApi;
import com.ta2khu75.thinkhub.follow.api.FollowDirection;
import com.ta2khu75.thinkhub.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.notification.api.NotificationApi;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TargetNotificationService {
	private final FollowApi followService;
	private final NotificationApi notificationService;

	public void notifyFollowersAboutNewTarget(NotificationRequest request) {
		Search search = new Search();
		search.setPage(0);
		search.setSize(1000);
		PageResponse<AuthorResponse> followerPage = followService.readPage(request.getAccountId(),
				FollowDirection.FOLLOWER, search);
		int totalPage = followerPage.getTotalPages();
		NotificationRequest notificationRequest = new NotificationRequest();
		BeanUtils.copyProperties(request, notificationRequest);
		do {
			for (AuthorResponse follower : followerPage.getContent()) {
//				notificationRequest.setAccountId(follower.id());
				notificationService.create(notificationRequest);
			}
			search.setPage(followerPage.getPage() + 1);
			followerPage = followService.readPage(request.getAccountId(), FollowDirection.FOLLOWER, search);
		} while (followerPage.getPage() < totalPage);
	}
}
