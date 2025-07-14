package com.ta2khu75.thinkhub.result.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

public record QuizResultRequest(Set<UserAnswerDto> userAnswers) {
}
