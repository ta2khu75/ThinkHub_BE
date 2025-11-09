package com.ta2khu75.thinkhub.notification.required.port;

import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;

public interface NotificationCommentPort {
	CommentResponse read(Long id);
}
