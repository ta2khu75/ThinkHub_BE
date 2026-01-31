package com.ta2khu75.thinkhub.quiz.draft.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuizDraftCreateRequest(@NotBlank(message = "Title must not be blank") String title,
		@NotNull(message = "Category id must not be null") Long categoryId) {
}