package com.ta2khu75.thinkhub.quiz.dto;

import java.time.Instant;
import java.util.List;

import com.ta2khu75.thinkhub.quiz.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.dto.BaseResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

public record QuizResponse(String title, Integer duration, String description, QuizLevel level,
		AccessModifier accessModifier, ResultVisibility resultVisibility, boolean shuffleQuestion, boolean completed,
		String id, Instant createdAt, Instant updatedAt, String imagePath, Long accountId, Long categoryId, Long postId,
		List<QuestionDto> questions) implements BaseResponse<String> {
}