package com.ta2khu75.thinkhub.modules.notification.api.dto;

import java.io.Serializable;

import com.ta2khu75.thinkhub.modules.notification.api.NotificationTargetType;

public record NotificationIdDto(String accountId, String targetId, NotificationTargetType targetType)
		implements Serializable {

}
