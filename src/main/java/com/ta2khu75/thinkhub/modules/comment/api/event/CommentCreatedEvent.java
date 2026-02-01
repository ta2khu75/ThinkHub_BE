package com.ta2khu75.thinkhub.modules.comment.api.event;

public record CommentCreatedEvent(Long userId, Long targetId) {
}
