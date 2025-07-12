package com.ta2khu75.thinkhub.quiz.dto;

import java.util.List;

import com.ta2khu75.thinkhub.quiz.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record QuizRequest(@NotBlank(message = "Title must not be blank") String title,
		@NotNull(message = "Time must not be null") Integer duration,
		@NotBlank(message = "Description must not be blank") String description,
		@NotNull(message = "Exam level must not be null") QuizLevel level, boolean shuffleQuestion, boolean completed,
		String blogId, @NotNull(message = "Quiz category must not be null") Long categoryId,
		@NotEmpty(message = "Question must not be empty") @Valid List<QuestionDto> questions,
		AccessModifier accessModifier, ResultVisibility resultVisibility) {
}