package com.ta2khu75.thinkhub.quiz.api.dto;

import java.util.List;

import com.ta2khu75.thinkhub.post.api.dto.PostResponse;

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