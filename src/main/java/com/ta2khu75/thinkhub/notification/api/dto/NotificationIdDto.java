package com.ta2khu75.thinkhub.notification.api.dto;

import java.io.Serializable;

import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;

public record NotificationIdDto(String accountId, String targetId, NotificationTargetType targetType)
		implements Serializable {

}
