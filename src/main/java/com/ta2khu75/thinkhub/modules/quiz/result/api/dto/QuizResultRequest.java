package com.ta2khu75.thinkhub.modules.quiz.result.api.dto;

import java.util.Set;

public record QuizResultRequest(Set<UserAnswerRequest> userAnswers) {
}
