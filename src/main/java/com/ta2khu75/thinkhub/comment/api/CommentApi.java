package com.ta2khu75.thinkhub.comment.api;

import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;

public interface CommentApi {
	CommentResponse read(Long id);
}
