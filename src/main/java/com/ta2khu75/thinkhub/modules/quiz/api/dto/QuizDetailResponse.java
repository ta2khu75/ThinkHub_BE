package com.ta2khu75.thinkhub.modules.quiz.api.dto;

import java.util.List;

import com.ta2khu75.thinkhub.modules.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuestionDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizDetailResponse extends QuizResponse {
	List<QuestionDto> questions;
	List<PostResponse> posts;
}