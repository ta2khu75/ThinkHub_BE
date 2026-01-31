package com.ta2khu75.thinkhub.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.notification.required.port.NotificationQuizPort;
import com.ta2khu75.thinkhub.quiz.api.QuizApi;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
class NotificationQuizClient extends BaseClient<QuizApi> implements NotificationQuizPort {

	protected NotificationQuizClient(QuizApi api) {
		super(api);
	}

	@Override
	public QuizResponse read(Long id) {
		return api.read(id);
	}

}
