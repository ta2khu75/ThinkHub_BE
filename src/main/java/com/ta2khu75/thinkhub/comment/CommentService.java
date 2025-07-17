package com.ta2khu75.thinkhub.comment;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

public interface CommentService {
	PageResponse<CommentResponse> readPageBy(Long targetId, CommentTargetType targetType, Search search);

	CommentResponse create(Long targetId, CommentTargetType targetType, CommentRequest request);

	CommentResponse update(Long id, CommentRequest request);

	CommentResponse read(Long id);

	ReportResponse report(Long id, ReportRequest request);

	void delete(Long id);
}
