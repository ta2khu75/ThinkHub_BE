package com.ta2khu75.thinkhub.result.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.result.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
	void deleteByQuestionId(Long questionId);
}
