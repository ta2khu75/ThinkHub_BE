package com.ta2khu75.thinkhub.modules.comment.api;

import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.modules.comment.api.model.CommentTargetType;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

public interface CommentApi {
	CommentResponse create(String targetId, CommentTargetType targetType, CommentRequest request);

	PageResponse<CommentResponse> readPageBy(String targetId, CommentTargetType targetType, Search search);

	CommentResponse read(Long id);

	CommentResponse update(Long id, CommentRequest request);

	void delete(Long id);

}
