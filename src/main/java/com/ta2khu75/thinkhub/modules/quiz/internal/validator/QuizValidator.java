package com.ta2khu75.thinkhub.modules.quiz.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.quiz.internal.entity.Quiz;
import com.ta2khu75.thinkhub.modules.quiz.internal.entity.QuizStatus;
import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

@Component
public class QuizValidator extends BaseValidator {
//	public void validateUpdate(Quiz quiz) {
//		ensure(quiz.getStatus() == QuizStatus.DRAFT, QuizErrorCode.NOT_EDITABLE, "Only DRAFT quiz can be updated");
//	}

}
