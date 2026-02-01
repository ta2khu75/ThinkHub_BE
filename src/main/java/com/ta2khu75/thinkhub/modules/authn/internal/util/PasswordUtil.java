package com.ta2khu75.thinkhub.modules.authn.internal.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordUtil {
	private static final SecureRandom random = new SecureRandom();

	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "!@#$%^&*()-_=+";

	private PasswordUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String generate(int length) {
		if (length < 8)
			throw new IllegalArgumentException("Minimum password length is 8");

		List<Character> password = new ArrayList<>();

		password.add(randomChar(UPPER));
		password.add(randomChar(LOWER));
		password.add(randomChar(DIGITS));
		password.add(randomChar(SPECIAL));

		String allChars = UPPER + LOWER + DIGITS + SPECIAL;
		for (int i = 4; i < length; i++) {
			password.add(randomChar(allChars));
		}

		Collections.shuffle(password, random);
		StringBuilder result = new StringBuilder();
		password.forEach(result::append);
		return result.toString();
	}

	private static char randomChar(String pool) {
		return pool.charAt(random.nextInt(pool.length()));
	}

}
