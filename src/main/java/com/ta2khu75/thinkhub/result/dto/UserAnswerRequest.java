package com.ta2khu75.thinkhub.result.dto;

import java.util.Set;

public record UserAnswerRequest(Long questionId, Set<Long> answerIds) {
}
