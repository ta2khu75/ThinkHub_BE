package com.ta2khu75.thinkhub.modules.quiz.draft.api.dto;

import java.time.Instant;
import java.util.List;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.domain.QuestionDraft;

public record QuizDraftResponse(String id, String title, Integer duration, String description, Long mediaId,
		String imageUrl, QuizLevel level, Long categoryId, List<String> tagNames, List<QuestionDraft> questions,
		Instant lastModifiedAt) {
}
