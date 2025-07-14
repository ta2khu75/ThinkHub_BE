package com.ta2khu75.thinkhub.quiz.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;

public interface QuizRepositoryCustom {
	Page<Quiz> search(QuizSearch search);
}
