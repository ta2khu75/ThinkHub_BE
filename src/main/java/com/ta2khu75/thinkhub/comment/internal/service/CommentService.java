package com.ta2khu75.thinkhub.comment.internal.service;

import com.ta2khu75.thinkhub.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.internal.entity.CommentTargetType;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

public interface CommentService {
	PageResponse<CommentResponse> readPageBy(Long targetId, CommentTargetType targetType, Search search);

	CommentResponse create(Long targetId, CommentTargetType targetType, CommentRequest request);

	CommentResponse update(Long id, CommentRequest request);

	void delete(Long id);
}
