package com.ta2khu75.thinkhub.notification.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.notification.NotificationStatus;
import com.ta2khu75.thinkhub.shared.dto.BaseClassResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse implements BaseClassResponse<NotificationIdDto> {
	NotificationIdDto id;
	Instant createdAt;
	Instant updatedAt;
	NotificationStatus status;
	Object target;
}
