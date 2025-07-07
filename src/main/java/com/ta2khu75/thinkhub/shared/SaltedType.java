package com.ta2khu75.thinkhub.shared;


public enum SaltedType {
	BLOG(47, 113), QUIZ(71, 13);
	private final int salt;
	private final int offset;
	SaltedType(int salt, int offset) {
        this.salt = salt;
        this.offset = offset;
    }

    public int getSalt() {
        return salt;
    }
    public int getOffset() {
		return offset;
	}
}
