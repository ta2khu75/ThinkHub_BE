package com.ta2khu75.thinkhub.result.internal.service;


import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.result.api.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizResultCache {
	private final RedisService redisService;
	public void save(String key,QuizResultResponse result) {
		redisService.setValue(key,result, result.getEndTime() );
    }

    public QuizResultResponse findById(String key) {
        return redisService.getValue(key, QuizResultResponse.class);
    }

    public void delete(String key) {
    	redisService.delete(key);
    }
}
