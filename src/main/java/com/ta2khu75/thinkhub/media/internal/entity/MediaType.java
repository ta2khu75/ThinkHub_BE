package com.ta2khu75.thinkhub.media.internal.entity;

public enum MediaType {
	IMAGE, VIDEO, AUDIO, DOCUMENT, OTHER;

	public static MediaType fromMimeType(String mimeType) {
		if (mimeType == null)
			return OTHER;
		if (mimeType.startsWith("image/"))
			return IMAGE;
		if (mimeType.startsWith("video/"))
			return VIDEO;
		if (mimeType.startsWith("audio/"))
			return AUDIO;
		if (mimeType.equals("application/pdf"))
			return DOCUMENT;
		if (mimeType.startsWith("application/msword") || mimeType.startsWith("application/vnd"))
			return DOCUMENT;
		return OTHER;
	}
}
