package com.ta2khu75.thinkhub.modules.comment.api;

import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentResponse;

public interface CommentApi {
	CommentResponse read(Long id);
}
