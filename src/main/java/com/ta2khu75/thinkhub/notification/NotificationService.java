package com.ta2khu75.thinkhub.notification;

import org.springframework.data.domain.Pageable;

import com.ta2khu75.quiz.model.response.NotificationResponse;
import com.ta2khu75.quiz.model.response.PageResponse;

public interface NotificationService extends CrudService<Notification, NotificationResponse> {
	PageResponse<NotificationResponse> readPageByAccountId(String accountId, Pageable pageable);
}
