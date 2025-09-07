package com.ta2khu75.thinkhub.quiz.api.dto;

import java.time.Instant;
import java.util.Set;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.api.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResponse implements BaseClassResponse<String> {
	String id;
	String title;
	Integer duration;
	String description;
	QuizLevel level;
	String imageUrl;
	Long categoryId;
	Set<TagDto> tags;
	AuthorResponse author;
	AccessModifier accessModifier;
	ResultVisibility resultVisibility;
	boolean shuffleQuestion;
	boolean completed;
	Instant createdAt;
	Instant updatedAt;
}