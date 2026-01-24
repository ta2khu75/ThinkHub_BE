package com.ta2khu75.thinkhub.quiz.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.api.enums.ResultVisibility;
import com.ta2khu75.thinkhub.quiz.internal.entity.QuizStatus;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResponse extends BaseClassResponse<String> {
	String title;
	String slug;
	Integer duration;
	QuizStatus status;
	QuizLevel level;
	String imageUrl;
	Long categoryId;
	Set<TagDto> tags;
	String description;
	AuthorResponse author;
	boolean shuffleQuestion;
	ResultVisibility resultVisibility;
}