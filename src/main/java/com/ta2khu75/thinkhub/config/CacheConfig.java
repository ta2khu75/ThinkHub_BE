package com.ta2khu75.thinkhub.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

//	@Bean
//	CacheManager cacheManager() {
//
//		SimpleCacheManager cacheManager = new SimpleCacheManager();
//		CaffeineCache dynamicTtlCache = new CaffeineCache("dynamicCache",
//				Caffeine.newBuilder().expireAfter(new Expiry<>() {
//					@Override
//					public long expireAfterCreate(Object key, Object value, long currentTime) {
//						if (value instanceof HasCustomTtl ttlValue) {
//							return TimeUnit.SECONDS.toNanos(ttlValue.getTtlSeconds());
//						}
//						return TimeUnit.MINUTES.toNanos(5); // TTL mặc định
//					}
//
//					@Override
//					public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
//						return currentDuration;
//					}
//
//					@Override
//					public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
//						return currentDuration;
//					}
//				}).build());
//
//		// Cache TTL cố định 10 phút
//		CaffeineCache fixed10MinCache = new CaffeineCache("fixed10MinCache",
//				Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000).build());
//
//		// Cache TTL cố định 1 giờ
//		CaffeineCache fixed1HourCache = new CaffeineCache("fixed1HourCache",
//				Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(5000).build());
//
//		// Có thể thêm cache khác nếu cần
//		cacheManager.setCaches(Arrays.asList(dynamicTtlCache, fixed10MinCache, fixed1HourCache));
//		return cacheManager;
//	}

	@Bean
	@Primary
	public CacheManager cacheManager(CacheManager caffeineCacheManager, CacheManager redisCacheManager) {
		CompositeCacheManager manager = new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
		manager.setFallbackToNoOpCache(false);
		return manager;
	}

	@Bean
	Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder().initialCapacity(100).maximumSize(10_000).expireAfterWrite(5, TimeUnit.MINUTES)
				.recordStats();
	}

	@Bean
	CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
		CaffeineCacheManager manager = new CaffeineCacheManager();
		manager.setCaffeine(caffeine);
		return manager;
	}

	@Bean
	RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))
				.disableCachingNullValues();
		return RedisCacheManager.builder(factory).cacheDefaults(config).build();
	}

}
