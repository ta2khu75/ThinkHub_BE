package com.ta2khu75.thinkhub.quiz.api;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;

public interface QuizApi {
	QuizResponse read(Long id);
	QuizDetailResponse readDetail(Long id);
}