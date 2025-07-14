package com.ta2khu75.thinkhub.result;

import com.ta2khu75.thinkhub.result.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizResultService extends SearchService<QuizResultSearch, QuizResultResponse> {
	QuizResultResponse take(Long quizId);

	QuizResultResponse submit(Long id, QuizResultRequest request);

	QuizResultResponse readDetail(Long id);
}
