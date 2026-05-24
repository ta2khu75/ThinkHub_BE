package com.ta2khu75.thinkhub.modules.quiz.draft.api.dto;

import java.util.List;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.domain.QuestionDraft;
	
public record QuizDraftUpdateRequest(String title, String description, Integer duration, QuizLevel level,
		Long categoryId, Long mediaId, List<String> tagNames, List<QuestionDraft> questions) {
}
