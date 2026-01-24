package com.ta2khu75.thinkhub.comment.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.comment.internal.entity.Comment;
import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentValidator extends BaseValidator {
	public void assertUpdatingOwnUser(Long currentUserId, Long targetUserId) {
		ensure(currentUserId.equals(targetUserId), CommentErrorCode.UPDATE_FORBIDDEN, "You can't update your own user");
	}

	public void validateUpdate(Comment comment, Long currentUserId) {
		ensure(comment.getAuthorId().equals(currentUserId), CommentErrorCode.NOT_AUTHOR,
				"You are not allowed to update this comment");
	}
}
