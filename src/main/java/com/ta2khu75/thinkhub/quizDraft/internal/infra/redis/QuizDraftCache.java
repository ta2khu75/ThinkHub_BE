package com.ta2khu75.thinkhub.quizDraft.internal.infra.redis;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.quizDraft.internal.domain.QuizDraft;
import com.ta2khu75.thinkhub.shared.service.AbstractRedis;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisCore;

@Component
public class QuizDraftCache extends AbstractRedis<QuizDraft> {
	public QuizDraftCache(RedisCore redisCore) {
		super(redisCore, QuizDraft.class);
	}

	@Override
	protected String prefix() {
		return "quiz:draft:";
	}

	@Override
	protected Duration ttl() {
		return Duration.ofDays(7);
	}

}
