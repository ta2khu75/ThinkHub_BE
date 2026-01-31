package com.ta2khu75.thinkhub.quiz.api.dto;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.api.enums.ResultVisibility;
import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuestionDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record QuizRequest(@NotBlank(message = "Title must not be blank") String title,
		@NotNull(message = "Time must not be null") Integer duration,
		@NotBlank(message = "Description must not be blank") String description,
		@NotNull(message = "Exam level must not be null") QuizLevel level,
		@NotNull(message = "Quiz category must not be null") Long categoryId, Long mediaId,
		@NotEmpty(message = "Tag must not be empty") @Valid Set<String> tags, List<String> postIds,
		@NotEmpty(message = "Question must not be empty") @Valid List<QuestionDto> questions, boolean shuffleQuestion,
		ResultVisibility resultVisibility) {
}