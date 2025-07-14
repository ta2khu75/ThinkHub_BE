package com.ta2khu75.thinkhub.result.dto;

import java.util.Set;

public record UserAnswerDto(Long questionId, boolean correct, Set<Long> answerIds) {
}
