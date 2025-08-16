package com.ta2khu75.thinkhub.result.api;

import com.ta2khu75.thinkhub.result.api.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.result.api.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.api.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizResultApi extends SearchService<QuizResultSearch, QuizResultResponse> {
	QuizResultResponse take(Long quizId);

	QuizResultResponse readByQuizId(Long quizId);

	QuizResultResponse submit(Long id, QuizResultRequest request);

	QuizResultResponse readDetail(Long id);

}
