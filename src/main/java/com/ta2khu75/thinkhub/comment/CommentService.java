package com.ta2khu75.thinkhub.comment;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

public interface CommentService {
	PageResponse<CommentResponse> readPageByBlogId(String blogId, Search search);

	CommentResponse create(String blogId, CommentRequest request);

	CommentResponse update(String id, CommentRequest request);

	void delete(String id);
}
