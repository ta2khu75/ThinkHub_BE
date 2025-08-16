package com.ta2khu75.thinkhub.result.api.dto;

import java.time.Instant;
import java.util.List;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResultResponse implements BaseClassResponse<String> {
	String id;
	Float score;
	Instant endTime;
	QuizResponse quiz;
	AuthorResponse account;
	Integer correctCount;
	List<UserAnswerResponse> userAnswers;
	Instant createdAt;
	Instant updatedAt;
}
