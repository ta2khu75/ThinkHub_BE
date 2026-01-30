package com.ta2khu75.thinkhub.shared.service;

import java.time.Duration;

import com.ta2khu75.thinkhub.shared.service.clazz.RedisCore;

public abstract class AbstractRedis<T> {

	protected final RedisCore redisCore;
	private final Class<T> clazz;

	protected AbstractRedis(RedisCore redisCore, Class<T> clazz) {
		this.redisCore = redisCore;
		this.clazz = clazz;
	}

	/** quiz:draft | post:draft | user:session */
	protected abstract String prefix();

	/** default TTL */
	protected abstract Duration ttl();

	protected String key(Object id) {
		return prefix() + ":" + id;
	}

	/* ================= CORE OPS ================= */

	public void save(Object id, T value) {
		redisCore.set(key(id), value, ttl());
	}

	public void save(Object id, T value, Duration customTtl) {
		redisCore.set(key(id), value, customTtl);
	}

	public T get(Object id) {
		return redisCore.get(key(id), clazz);
	}

	public boolean exists(Object id) {
		return redisCore.exists(key(id));
	}

	public boolean delete(Object id) {
		return redisCore.delete(key(id));
	}

	/** autosave use-case */
	public void refreshTtl(Object id) {
		T value = get(id);
		if (value != null) {
			save(id, value);
		}
	}
}
