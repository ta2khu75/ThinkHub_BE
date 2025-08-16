package com.ta2khu75.thinkhub.result.api.dto;

import java.util.Set;

public record QuizResultRequest(Set<UserAnswerRequest> userAnswers) {
}
