package com.ta2khu75.thinkhub.shared.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {
	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
	private static final Pattern MULTIPLE_DASH = Pattern.compile("[-]{2,}");

	private SlugUtil() {
	}

	/**
	 * Convert input string to a URL-friendly slug. - Normalize unicode (NFD) and
	 * strip diacritics - Replace whitespace with single dash - Remove
	 * non-latin/number/underscore/hyphen characters - Collapse multiple dashes and
	 * trim leading/trailing dashes
	 */
	public static String toSlug(String input) {
		if (input == null)
			return "";
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
		// remove diacritics
		String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		// lower-case
		slug = slug.toLowerCase(Locale.ENGLISH);
		// remove invalid characters
		slug = NONLATIN.matcher(slug).replaceAll("");
		// collapse multiple dashes
		slug = MULTIPLE_DASH.matcher(slug).replaceAll("-");
		// trim leading/trailing dashes
		slug = slug.replaceAll("(^-|-$)", "");
		return slug;
	}
}
