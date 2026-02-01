package com.ta2khu75.thinkhub.modules.quiz.internal.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.modules.quiz.internal.entity.Quiz;

public interface QuizRepositoryCustom {
	Page<Quiz> search(QuizSearch search);
}
