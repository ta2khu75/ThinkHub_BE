package com.ta2khu75.thinkhub.modules.follow.api.event;

import com.ta2khu75.thinkhub.modules.notification.api.model.NotificationTarget;

public record FollowTargetCreatedEvent(Long userId, Long targetId, NotificationTarget target) {

}
