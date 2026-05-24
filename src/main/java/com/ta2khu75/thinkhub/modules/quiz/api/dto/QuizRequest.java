package com.ta2khu75.thinkhub.modules.quiz.api.dto;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.api.model.ResultVisibility;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuestionDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record QuizRequest(@NotBlank(message = "Title must not be blank") String title,
		@NotBlank(message = "Description must not be blank") String description,
		@NotNull(message = "Time must not be null") Integer duration,
		@NotNull(message = "Quiz category must not be null") Long categoryId, Long mediaId,
		@NotEmpty(message = "Tag must not be empty") @Valid Set<String> tags, List<String> postIds,
		@NotEmpty(message = "Question must not be empty") @Valid List<QuestionDto> questions, Boolean shuffleQuestion,
		@NotNull(message = "Exam level must not be null") QuizLevel level, ResultVisibility resultVisibility) {
}