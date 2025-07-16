package com.ta2khu75.thinkhub.quiz;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.service.CrudFileService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizService extends CrudFileService<QuizRequest, QuizResponse, Long>,
		SearchService<QuizSearch, QuizResponse>, ExistsService<Long> {
	QuizResponse readDetail(Long id);

	CommentResponse comment(Long id, CommentRequest request);

	ReportResponse report(Long id, ReportRequest request);

	PageResponse<CommentResponse> readPageComments(Long targetId, Search search);
//	List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId,String keyword);
//	QuizResponse readDetail(String id);
//	List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId,String keyword);
//	List<QuizResponse> myReadAllById(List<String> ids);
//	Long countByAuthorEmail(String authorEmail);
//	Long countByAuthorIdAndAccessModifier(String authorId, AccessModifier accessModifier);
}
