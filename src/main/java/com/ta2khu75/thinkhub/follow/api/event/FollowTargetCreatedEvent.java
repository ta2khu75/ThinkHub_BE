package com.ta2khu75.thinkhub.follow.api.event;

import com.ta2khu75.thinkhub.notification.api.NotificationTargetType;

public record FollowTargetCreatedEvent(Long userId, Long targetId, NotificationTargetType targetType) {

}
