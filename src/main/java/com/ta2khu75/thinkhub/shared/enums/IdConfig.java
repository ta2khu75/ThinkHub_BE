package com.ta2khu75.thinkhub.shared.enums;

public enum IdConfig {
	BLOG(47, 1031, "0rt6ATGHZYuM1KoFJvjUIOkgmXbP7qh4iVReaS9cx3N8LfW2zdCwQpBnE5yDls"),
	QUIZ(71, 1201, "t5EYmcbhNiZMGuQU9o4IKXJqayD7CRkOgwPlWn2Hx3Sd6vTsBzVpfeA1r80LFj"),
	ACCOUNT(11, 1129, "rxi3a47NQb815JqPWFnZeRhYpymOwljCvg0UukAMfLdEXG9SHsDtI6BoTzcKV2");

	private final int salt;
	private final int offset;
	private final String alphabet;

	private IdConfig(int salt, int offset, String alphabet) {
		this.salt = salt;
		this.offset = offset;
		this.alphabet = alphabet;
	}

	public int getSalt() {
		return salt;
	}

	public int getOffset() {
		return offset;
	}
	public String getAlphabet() {
		return alphabet;
	}
}
