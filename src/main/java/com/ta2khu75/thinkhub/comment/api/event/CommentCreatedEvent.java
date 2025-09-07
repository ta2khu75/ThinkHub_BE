package com.ta2khu75.thinkhub.comment.api.event;

public record CommentCreatedEvent(Long userId, Long targetId) {
}
