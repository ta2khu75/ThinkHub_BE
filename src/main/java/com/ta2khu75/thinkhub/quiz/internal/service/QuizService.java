package com.ta2khu75.thinkhub.quiz.internal.service;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface QuizService extends CrudService<QuizRequest, QuizResponse, String>,
		SearchService<QuizSearch, QuizResponse>, ExistsService<String> {

	QuizDetailResponse readDetail(String id);

	void disable(String id);

	void publish(String id);

	void hide(String id);
}
