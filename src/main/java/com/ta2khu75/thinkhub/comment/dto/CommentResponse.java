package com.ta2khu75.thinkhub.comment.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.shared.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse implements BaseClassResponse<Long> {
	Long id;
	String content;
	AuthorResponse author;
	Instant createdAt;
	Instant updatedAt;
}
