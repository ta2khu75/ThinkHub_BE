package com.ta2khu75.thinkhub.result.api.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResultDetailResponse extends QuizResultResponse {
	List<UserAnswerResponse> userAnswers;
}
