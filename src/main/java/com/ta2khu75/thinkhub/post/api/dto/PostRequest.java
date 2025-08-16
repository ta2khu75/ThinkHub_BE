package com.ta2khu75.thinkhub.post.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PostRequest(@NotNull(message = "Title must not be null") String title,
		@NotNull(message = "Content must not be null") String content, Set<String> quizIds,
		@NotNull(message = "Category id must not be null") Long categoryId,
		@NotNull(message = "Access modifier must not be null") AccessModifier accessModifier,
		@NotEmpty(message = "Blog tags must not be empty") Set<String> tags) {
}
