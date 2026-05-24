package com.ta2khu75.thinkhub.modules.post.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.modules.post.internal.domain.PostStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PostRequest(@NotNull(message = "Title must not be null") String title,
		@NotNull(message = "Content must not be null") String content, Set<String> quizIds,
		@NotNull(message = "Category id must not be null") Long categoryId, Long mediaId,
		@NotNull(message = "Status must not be null") PostStatus status,
		@NotEmpty(message = "Blog tags must not be empty") Set<String> tags) {
}
