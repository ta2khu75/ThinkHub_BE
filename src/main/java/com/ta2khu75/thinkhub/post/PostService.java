package com.ta2khu75.thinkhub.post;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.post.dto.PostRequest;
import com.ta2khu75.thinkhub.post.dto.PostResponse;
import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface PostService extends CrudService<PostRequest, PostResponse, Long>,
		SearchService<PostSearch, PostResponse>, ExistsService<Long> {
	CommentResponse comment(Long id, CommentRequest request);

	ReportResponse report(Long id, ReportRequest request);

	PageResponse<CommentResponse> readPageComments(Long targetId, Search search);
}
