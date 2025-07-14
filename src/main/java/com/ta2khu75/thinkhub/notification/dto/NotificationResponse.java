package com.ta2khu75.thinkhub.notification.dto;

import java.time.Instant;

import com.ta2khu75.quiz.model.NotificationStatus;
import com.ta2khu75.quiz.model.entity.id.NotificationId;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse implements BaseResponse<NotificationId> {
	NotificationId id;
	Instant createdAt;
	Instant updatedAt;
	NotificationStatus status;
	Object target;
}
