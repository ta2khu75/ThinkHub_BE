package com.ta2khu75.thinkhub.result.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.result.internal.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
	void deleteByQuestionId(Long questionId);
}
