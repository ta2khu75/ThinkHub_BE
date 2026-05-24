package com.ta2khu75.thinkhub.modules.quiz.internal.validator;

import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Answer;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum AnswerErrorCode implements ErrorCode {
	INVALID_CONTENT;

	@Override
	public String getCode() {
		return format(Answer.class, name());
	}
}
