package com.ta2khu75.thinkhub.modules.quiz.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.api.model.ResultVisibility;
import com.ta2khu75.thinkhub.modules.quiz.internal.domain.QuizStatus;
import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.BaseClassResponse;

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