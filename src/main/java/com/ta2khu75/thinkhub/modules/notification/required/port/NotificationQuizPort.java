package com.ta2khu75.thinkhub.modules.notification.required.port;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizResponse;

public interface NotificationQuizPort {
	QuizResponse read(Long id);
}
