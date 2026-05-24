package com.ta2khu75.thinkhub.modules.quiz.internal.validator;

import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Question;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum QuestionErrorCode implements ErrorCode {
	INVALID_CONTENT, INVALID_ANSWERS, NO_CORRECT_ANSWER;

	@Override
	public String getCode() {
		return format(Question.class, name());
	}

}
