package com.ta2khu75.thinkhub.shared.util;

import java.util.List;

import org.sqids.Sqids;

import com.ta2khu75.thinkhub.shared.enums.IdConfig;

public class IdConverterUtil {

	private static Sqids getSqids(String alphabet) {
		return Sqids.builder().minLength(7).alphabet(alphabet).build();
	}

	public static String encode(Long id, IdConfig idConfig) {
		if (id == null || idConfig == null) {
			throw new IllegalArgumentException("id and saltedType must not be null");
		}

		String result = encode(getSqids(idConfig.getAlphabet()), id * idConfig.getSalt() + idConfig.getOffset());
		return result;
	}

	public static long decode(String encodedStr, IdConfig idConfig) {
		if (encodedStr == null || encodedStr.isBlank() || idConfig == null) {
			throw new IllegalArgumentException("encodedStr and saltedType must not be null or blank");
		}
		long id = decode(getSqids(idConfig.getAlphabet()), encodedStr);
		return (id - idConfig.getOffset()) / idConfig.getSalt();
	}

	private static String encode(Sqids sqids, long id) {
		if (id > Long.MAX_VALUE || id < 0) {
			throw new IllegalArgumentException("ID must be between 0 and " + Long.MAX_VALUE);
		}
		return sqids.encode(List.of(id));
	}

	// Decode string về id long
	private static long decode(Sqids sqids, String code) {
		List<Long> result = sqids.decode(code);
		if (result.size() == 0) {
			throw new IllegalArgumentException("Invalid code: " + code);
		}
		return result.get(0);
	}
}
