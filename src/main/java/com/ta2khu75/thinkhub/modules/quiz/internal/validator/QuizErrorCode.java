package com.ta2khu75.thinkhub.modules.quiz.internal.validator;

import com.ta2khu75.thinkhub.modules.quiz.internal.entity.Quiz;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum QuizErrorCode implements ErrorCode {
	NOT_FOUND, STATUS_INVALID, NOT_EDITABLE;

	@Override
	public String getCode() {
		return Quiz.class.getSimpleName() + ":" + name();
	}

}
