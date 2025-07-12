package com.ta2khu75.thinkhub.post.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.tag.dto.TagRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BlogRequest(@NotNull(message = "Content must not be null") String content, Set<String> quizIds,
		@NotNull(message = "Title must not be null") String title,
		@NotNull(message = "Access modifier must not be null") AccessModifier accessModifier,
		@NotEmpty(message = "Blog tags must not be empty") Set<TagRequest> tags) {
}
