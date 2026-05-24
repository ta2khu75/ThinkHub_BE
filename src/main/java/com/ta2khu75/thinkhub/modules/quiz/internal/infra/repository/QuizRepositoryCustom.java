package com.ta2khu75.thinkhub.modules.quiz.internal.infra.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Quiz;

public interface QuizRepositoryCustom {
	Page<Quiz> search(QuizSearch search);
}
