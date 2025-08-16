package com.ta2khu75.thinkhub.quiz.internal.repository;


import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizCache {
	private final RedisService redisService;
	public void save(Long id,QuizResponse quiz) {
		redisService.setValue(RedisKeyBuilder.quiz(id), quiz, Instant.now().plusSeconds((quiz.getDuration() + 1) * 60L));
    }

    public QuizResponse findById(Long id) {
        return redisService.getValue(RedisKeyBuilder.quiz(id), QuizResponse.class);
    }

    public void delete(Long id) {
    	redisService.delete(RedisKeyBuilder.quiz(id));
    }
}
