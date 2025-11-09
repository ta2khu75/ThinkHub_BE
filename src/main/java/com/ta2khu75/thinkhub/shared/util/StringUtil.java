package com.ta2khu75.thinkhub.shared.util;

public class StringUtil {
	private StringUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String toUpperSnakeCase(String input) {
		return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
	}
}
