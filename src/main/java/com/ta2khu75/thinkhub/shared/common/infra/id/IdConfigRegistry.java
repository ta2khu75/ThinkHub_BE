package com.ta2khu75.thinkhub.shared.common.infra.id;

import java.util.Map;

import com.ta2khu75.thinkhub.modules.post.internal.domain.Post;
import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Quiz;
import com.ta2khu75.thinkhub.modules.user.internal.domain.User;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

public final class IdConfigRegistry {
	private static final Map<Class<?>, IdConfig> REGISTRY = Map.of(User.class, IdConfig.USER, Post.class, IdConfig.POST,
			Quiz.class, IdConfig.QUIZ);

	public static IdConfig resolve(IdSubject type) {
		return switch (type) {
		case POST -> IdConfig.POST;
		case QUIZ -> IdConfig.QUIZ;
		case USER -> IdConfig.USER;
		case QUIZ_RESULT -> IdConfig.QUIZ_RESULT;
		default -> throw new IllegalArgumentException("Unexpected value: " + type);
		};
	}
}
