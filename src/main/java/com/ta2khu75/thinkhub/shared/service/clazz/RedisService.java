package com.ta2khu75.thinkhub.shared.service.clazz;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	// ========= VALUE (String-like) =========

	public <T> void setValue(String key, T value, Duration ttl) {
		redisTemplate.opsForValue().set(key, value, ttl);
	}

	public <T> void setValue(String key, T value, Instant ttl) {
		redisTemplate.opsForValue().set(key, value, Duration.between(Instant.now(), ttl));
	}

	public <T> void setValue(String key, T value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public <T> T getValue(String key, Class<T> clazz) {
		Object value = redisTemplate.opsForValue().get(key);
		if (value == null)
			return null;

		if (clazz.isInstance(value)) {
			return clazz.cast(value);
		}

		log.warn("Redis value for key [{}] is not of type {}", key, clazz.getSimpleName());
		return null;
	}

	public <T> T getOrDefault(String key, Class<T> clazz, T defaultValue) {
		T result = getValue(key, clazz);
		return result != null ? result : defaultValue;
	}

	public boolean delete(String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}

	// ========= HASH =========

	public <T> void putHash(String key, String hashKey, T value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	public <T> T getHash(String key, String hashKey, Class<T> clazz) {
		Object value = redisTemplate.opsForHash().get(key, hashKey);
		return clazz.cast(value);
	}

	public Map<Object, Object> getAllHash(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public void deleteHash(String key, String hashKey) {
		redisTemplate.opsForHash().delete(key, hashKey);
	}

	// ========= LIST =========

	public <T> void pushToListLeft(String key, T value) {
		redisTemplate.opsForList().leftPush(key, value);
	}

	public <T> void pushToListRight(String key, T value) {
		redisTemplate.opsForList().rightPush(key, value);
	}

	public List<Object> getListRange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	public Object popFromListLeft(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	public Object popFromListRight(String key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	// ========= SET =========

	public <T> void addToSet(String key, T value) {
		redisTemplate.opsForSet().add(key, value);
	}

	public Set<Object> getSetMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	public boolean isMemberOfSet(String key, Object value) {
		return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
	}

	public void removeFromSet(String key, Object value) {
		redisTemplate.opsForSet().remove(key, value);
	}

	// ========= TTL =========

	public boolean exists(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public boolean expire(String key, Duration ttl) {
		return Boolean.TRUE.equals(redisTemplate.expire(key, ttl));
	}

	public Duration getTtl(String key) {
		Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
		return expire != null && expire >= 0 ? Duration.ofSeconds(expire) : null;
	}

	public void setExpiration(String key, Duration ttl) {
		redisTemplate.expire(key, ttl);
	}

	public class RedisKeyBuilder {
		public static String refreshToken(String id) {
			return "auth:refresh:" + id;
		}

		public static String role(Long id) {
			return "authority:role:" + id;
		}

		public static String accountLock(Long id) {
			return "account:lock:" + id;
		}

		public static String quiz(Long id) {
			return "quiz:" + id;
		}

		public static String quizResult(Long accountId, Long quizId) {
			return "quiz:" + quizId + ":account:" + accountId + ":result";
		}
	}
}