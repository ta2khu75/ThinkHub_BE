package com.ta2khu75.thinkhub.modules.comment.internal.validator;

import com.ta2khu75.thinkhub.modules.comment.internal.domain.Comment;
import com.ta2khu75.thinkhub.shared.domain.enums.ErrorCode;

public enum CommentErrorCode implements ErrorCode {
	NOT_AUTHOR, NOT_FOUND, UPDATE_FORBIDDEN;

	@Override
	public String getCode() {
		return Comment.class.getSimpleName() + ":" + name();
	}

}
