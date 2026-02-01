package com.ta2khu75.thinkhub.modules.notification.required.port;

import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentResponse;

public interface NotificationCommentPort {
	CommentResponse read(Long id);
}
