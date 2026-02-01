package com.ta2khu75.thinkhub.modules.follow.api.event;

import com.ta2khu75.thinkhub.modules.notification.api.NotificationTargetType;

public record FollowTargetCreatedEvent(Long userId, Long targetId, NotificationTargetType targetType) {

}
