package com.ta2khu75.thinkhub.shared.util;

import java.util.List;

import org.sqids.Sqids;
import com.ta2khu75.thinkhub.shared.SaltedType;

public class SqidsUtil {
	private SqidsUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static final Sqids SQIDS = Sqids.builder().minLength(7)
			.alphabet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").build();

	public static String encodeWithSalt(Long id,SaltedType  saltedType) {
		System.out.println(id);
		System.out.println(saltedType.name());
		String result = encode(id * saltedType.getSalt() + saltedType.getOffset());
		System.out.println(result);
		return result;
	}

	public static long decodeWithSalt(String str, SaltedType saltedType) {
		System.out.println(str);
		System.out.println(saltedType.name());
		long id = decode(str);
		long idReturn = (id - saltedType.getOffset()) / saltedType.getSalt();
		System.out.println(idReturn);
		return idReturn;
	}

	public static String encode(long id) {
		if (id > Long.MAX_VALUE || id < 0) {
			throw new IllegalArgumentException("ID must be between 0 and " + Long.MAX_VALUE);
		}
		return SQIDS.encode(List.of(id));
	}

	// Decode string về id long
	public static long decode(String code) {
		List<Long> result = SQIDS.decode(code);
		if (result.size() == 0) {
			throw new IllegalArgumentException("Invalid code: " + code);
		}
		return result.get(0);
	}
}
