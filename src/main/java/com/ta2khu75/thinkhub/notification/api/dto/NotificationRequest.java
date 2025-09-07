package com.ta2khu75.thinkhub.notification.api.dto;

import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
	private Long userId;
	private Long targetId;
	private NotificationTargetType targetType;
}
