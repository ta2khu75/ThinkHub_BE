package com.ta2khu75.thinkhub.shared.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sqids.Sqids;

import com.ta2khu75.thinkhub.shared.enums.IdConfig;

public class IdConverterUtil {

	private static final Map<String, Sqids> sqidsCache = new ConcurrentHashMap<>();

	private IdConverterUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static Sqids getSqids(IdConfig idConfig) {
		return sqidsCache.computeIfAbsent(idConfig.getAlphabet(),
				key -> Sqids.builder().minLength(7).alphabet(idConfig.getAlphabet()).build());
	}

	public static String encode(Long id, IdConfig idConfig) {
		if (id == null || idConfig == null) {
			throw new IllegalArgumentException("id and idConfig must not be null");
		}
		long salted = id * idConfig.getSalt() + idConfig.getOffset();
		return encode(getSqids(idConfig), salted);
	}

	public static long decode(String encodedStr, IdConfig idConfig) {
		if (encodedStr == null || encodedStr.isBlank() || idConfig == null) {
			throw new IllegalArgumentException("encodedStr and idConfig must not be null or blank");
		}
		long decoded = decode(getSqids(idConfig), encodedStr);
		return (decoded - idConfig.getOffset()) / idConfig.getSalt();
	}

	private static String encode(Sqids sqids, long id) {
		if (id > Long.MAX_VALUE || id < 0) {
			throw new IllegalArgumentException("ID must be between 0 and " + Long.MAX_VALUE);
		}
		return sqids.encode(List.of(id));
	}

	private static long decode(Sqids sqids, String code) {
		List<Long> result = sqids.decode(code);
		if (result.isEmpty()) {
			throw new IllegalArgumentException("Invalid code: " + code);
		}
		return result.get(0);
	}

}
