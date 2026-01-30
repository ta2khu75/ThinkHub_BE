package com.ta2khu75.thinkhub.quizDraft.internal.service;

import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftUpdateRequest;

public interface QuizDraftService {
	QuizDraftResponse create(QuizDraftCreateRequest request);

	void update(String id, QuizDraftUpdateRequest request);

	QuizDraftResponse read(String id);

	void delete(String id);
}
