package com.ta2khu75.thinkhub.modules.quiz.draft.internal.service;

import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftUpdateRequest;

public interface QuizDraftService {
	QuizDraftResponse create(QuizDraftCreateRequest request);

	void update(String id, QuizDraftUpdateRequest request);

	QuizDraftResponse read(String id);

	void delete(String id);
}
