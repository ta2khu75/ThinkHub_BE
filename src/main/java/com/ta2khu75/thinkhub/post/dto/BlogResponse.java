package com.ta2khu75.thinkhub.post.dto;

import java.time.Instant;
import java.util.Set;

import com.ta2khu75.quiz.model.AccessModifier;
import com.ta2khu75.quiz.model.base.BlogBase;
import com.ta2khu75.quiz.model.entity.BlogTag;
import com.ta2khu75.quiz.model.response.account.AccountProfileResponse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public record BlogResponse(@NotNull(message = "Title must not be null") String title,
		@NotNull(message = "Access modifier must not be null") AccessModifier accessModifier,
		@NotEmpty(message = "Blog tags must not be empty")
		Set<BlogTag> tags, String id,
		Instant createdAt, Instant updatedAt,
		int viewCount,
		int commentCount,

				AccountProfileResponse author,
		String content,)implements BaseResponse<String> {
}
