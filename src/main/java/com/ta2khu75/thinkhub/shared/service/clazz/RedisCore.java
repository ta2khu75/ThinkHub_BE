package com.ta2khu75.thinkhub.shared.service.clazz;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCore {
	private final StringRedisTemplate redisTemplate;
	@Qualifier("redisObjectMapper")
	private final ObjectMapper objectMapper;

//	public void set(String key, Object value) {
//		try {
//			redisTemplate.opsForValue().set(key, toJson(value));
//		} catch (Exception e) {
//			log.error("Redis SET failed: {}", key, e);
//		}
//	}
//
//	public void set(String key, Object value, Duration ttl) {
//		try {
//			redisTemplate.opsForValue().set(key, toJson(value), ttl);
//		} catch (Exception e) {
//			log.error("Redis SET with TTL failed: {}", key, e);
//		}
//	}

	public <T> void set(String key, T value, Duration ttl) {
		try {
			String json = toJson(value);
			redisTemplate.opsForValue().set(key, json, ttl);
		} catch (Exception e) {
			throw new RuntimeException("Redis set failed", e);
		}
	}

	public <T> T get(String key, Class<T> clazz) {
		String json = redisTemplate.opsForValue().get(key);
		if (json == null)
			return null;
		try {
			return fromJson(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("Redis get failed", e);
		}
	}

	public <T> Optional<T> getOptional(String key, Class<T> type) {
		try {
			String json = redisTemplate.opsForValue().get(key);
			if (json == null)
				return Optional.empty();
			return Optional.of(fromJson(json, type));
		} catch (Exception e) {
			log.error("Redis GET failed: {}", key, e);
			return Optional.empty();
		}
	}

	public boolean delete(String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}

	public boolean exists(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public boolean expire(String key, Duration ttl) {
		return Boolean.TRUE.equals(redisTemplate.expire(key, ttl));
	}

	public Long ttl(String key) {
		return redisTemplate.getExpire(key);
	}

	/* ================= ATOMIC ================= */

	public boolean setIfAbsent(String key, Object value, Duration ttl) {
		try {
			return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, toJson(value), ttl));
		} catch (Exception e) {
			log.error("Redis SETNX failed: {}", key, e);
			return false;
		}
	}

	public long increment(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	public long increment(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/* ================= INTERNAL ================= */

	private String toJson(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	private <T> T fromJson(String json, Class<T> type) throws JsonProcessingException {
		return objectMapper.readValue(json, type);
	}

}
