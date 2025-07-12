package com.ta2khu75.thinkhub.quiz;

import java.util.List;

import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.shared.service.CrudFileService;
import com.ta2khu75.thinkhub.shared.service.SearchService;


public interface QuizService extends CrudFileService<QuizRequest, QuizResponse, String>, SearchService<QuizResponse, QuizSearch> {
	QuizResponse readDetail(String id);
	List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId,String keyword);
//	List<QuizResponse> myReadAllById(List<String> ids);
//	Long countByAuthorEmail(String authorEmail);
//	Long countByAuthorIdAndAccessModifier(String authorId, AccessModifier accessModifier);
}
