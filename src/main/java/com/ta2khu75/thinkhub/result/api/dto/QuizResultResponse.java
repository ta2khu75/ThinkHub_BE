package com.ta2khu75.thinkhub.result.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResultResponse extends BaseClassResponse<String> {
	Float score;
	Instant endTime;
	QuizDetailResponse quiz;
	AuthorResponse account;
	Integer correctCount;
}
