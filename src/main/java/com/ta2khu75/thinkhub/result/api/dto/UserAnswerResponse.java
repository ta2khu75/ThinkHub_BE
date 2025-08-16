package com.ta2khu75.thinkhub.result.api.dto;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAnswerResponse {
	Long id;
	boolean correct;
	Long questionId;
	Set<Long> answerIds;
}
