package com.ta2khu75.thinkhub.shared.common.infra.id;

import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

public final class IdConfigRegistry {

	public static IdConfig resolve(IdSubject type) {
		return switch (type) {
		case POST -> IdConfig.POST;
		case QUIZ -> IdConfig.QUIZ;
		case USER -> IdConfig.USER;
		case QUIZ_RESULT -> IdConfig.QUIZ_RESULT;
		default -> null;
		};
	}
}
