package com.ta2khu75.thinkhub.notification.dto;

import com.ta2khu75.thinkhub.notification.NotificationTargetType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
	private Long accountId;
	private Long targetId;
	private NotificationTargetType targetType;
}
