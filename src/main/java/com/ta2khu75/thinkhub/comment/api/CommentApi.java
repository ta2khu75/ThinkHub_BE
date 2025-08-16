package com.ta2khu75.thinkhub.comment.api;

import com.ta2khu75.thinkhub.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.internal.entity.CommentTargetType;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

public interface CommentApi {
	PageResponse<CommentResponse> readPageBy(Long targetId, CommentTargetType targetType, Search search);

	CommentResponse create(Long targetId, CommentTargetType targetType, CommentRequest request);

	CommentResponse update(Long id, CommentRequest request);

	CommentResponse read(Long id);

	void delete(Long id);
}
