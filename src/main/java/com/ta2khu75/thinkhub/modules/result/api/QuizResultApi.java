package com.ta2khu75.thinkhub.modules.result.api;

import com.ta2khu75.thinkhub.modules.result.api.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.modules.result.api.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.modules.result.api.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizResultApi extends SearchService<QuizResultSearch, QuizResultResponse> {
	QuizResultResponse take(String quizId);

	QuizResultResponse readByQuizId(String quizId);

	QuizResultResponse submit(String id, QuizResultRequest request);

	QuizResultResponse readDetail(String id);

}
