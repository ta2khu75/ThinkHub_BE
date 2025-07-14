package com.ta2khu75.thinkhub.post.dto;

import java.time.Instant;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.shared.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse implements BaseClassResponse<String> {
	String id;
	String title;
	String content;
	Set<TagDto> tags;
	AuthorResponse author;
	Long authorId;
	List<String> quizIds;
	Long categoryId;
	int viewCount;
	AccessModifier accessModifier;
	Instant createdAt;
	Instant updatedAt;
}