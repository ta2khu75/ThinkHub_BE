package com.ta2khu75.thinkhub.quiz.api;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizApi extends CrudService<QuizRequest, QuizResponse, Long>, SearchService<QuizSearch, QuizResponse>,
		ExistsService<Long> {
	QuizDetailResponse readDetail(Long id);
}
//	List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId,String keyword);
//	QuizResponse readDetail(String id);
//	List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId,String keyword);
//	List<QuizResponse> myReadAllById(List<String> ids);
//	Long countByAuthorEmail(String authorEmail);
//	Long countByAuthorIdAndAccessModifier(String authorId, AccessModifier accessModifier);q}
