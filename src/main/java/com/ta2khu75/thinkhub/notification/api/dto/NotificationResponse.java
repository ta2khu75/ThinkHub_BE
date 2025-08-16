package com.ta2khu75.thinkhub.notification.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.notification.api.NotificationStatus;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;

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
