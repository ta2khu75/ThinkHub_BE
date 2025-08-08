package com.ta2khu75.thinkhub.quiz.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizCacheService {
	private final RedisService redisService;
	private final QuizService quizService;

	public QuizResponse readQuizDetail(Long quizId) {
		String key = RedisKeyBuilder.quiz(quizId);
		QuizResponse quiz = redisService.getValue(key, QuizResponse.class);
		if (quiz == null) {
			quiz = quizService.readDetail(quizId);
			redisService.setValue(key, quiz, Instant.now().plusSeconds((quiz.getDuration() + 1) * 60L));
		}
		return quiz;
	}
}
