package com.ta2khu75.thinkhub.quiz.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.quiz.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

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
	Long authorId;
	List<String> postIds;
	AccessModifier accessModifier;
	ResultVisibility resultVisibility;
	boolean shuffleQuestion;
	boolean completed;
	Instant createdAt;
	Instant updatedAt;
	List<QuestionDto> questions;
}