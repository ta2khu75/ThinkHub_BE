package com.ta2khu75.thinkhub.postDraft.internal.infra.redis;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.postDraft.internal.domain.PostDraft;
import com.ta2khu75.thinkhub.shared.service.AbstractRedis;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisCore;

@Component
public class PostDraftCache extends AbstractRedis<PostDraft> {

	public PostDraftCache(RedisCore redisCore) {
		super(redisCore, PostDraft.class);
	}

	@Override
	protected String prefix() {
		return "post:draft:";
	}

	@Override
	protected Duration ttl() {
		return Duration.ofDays(7);
	}

}
