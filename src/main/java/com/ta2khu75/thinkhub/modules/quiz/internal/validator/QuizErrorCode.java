package com.ta2khu75.thinkhub.modules.quiz.internal.validator;

import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Quiz;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum QuizErrorCode implements ErrorCode {
	NOT_FOUND, INVALID_TITLE, INVALID_SLUG, INVALID_DESCRIPTION, INVALID_LEVEL, INVALID_OWNER, INVALID_CATEGORY,
	INVALID_DURATION, INVALID_RESULT_VISIBILITY, INVALID_QUESTION, STATUS_INVALID, NOT_EDITABLE;

	@Override
	public String getCode() {
		return Quiz.class.getSimpleName() + ":" + name();
	}

}
