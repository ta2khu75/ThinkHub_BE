package com.ta2khu75.thinkhub.modules.quiz.result.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizDetailResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.BaseClassResponse;

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
