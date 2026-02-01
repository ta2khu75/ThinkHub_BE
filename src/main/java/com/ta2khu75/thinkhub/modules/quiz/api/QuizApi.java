package com.ta2khu75.thinkhub.modules.quiz.api;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizResponse;

public interface QuizApi {
	QuizResponse read(Long id);
	QuizDetailResponse readDetail(Long id);
}