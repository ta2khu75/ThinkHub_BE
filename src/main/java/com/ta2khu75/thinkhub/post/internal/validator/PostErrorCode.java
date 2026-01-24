package com.ta2khu75.thinkhub.post.internal.validator;

import com.ta2khu75.thinkhub.post.internal.entity.Post;
import com.ta2khu75.thinkhub.shared.enums.ErrorCode;

public enum PostErrorCode implements ErrorCode {
	STATUS_INVALID,;

	@Override
	public String getCode() {
		return Post.class.getSimpleName() + ":" + this.name();
	}
}
