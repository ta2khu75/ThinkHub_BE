package com.ta2khu75.thinkhub.comment.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
	@NotNull(message = "Content must not be null")
	String content;
}
