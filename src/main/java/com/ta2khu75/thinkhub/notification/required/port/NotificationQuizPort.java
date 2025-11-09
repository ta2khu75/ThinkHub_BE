package com.ta2khu75.thinkhub.notification.required.port;

import com.ta2khu75.thinkhub.quiz.api.dto.QuizResponse;

public interface NotificationQuizPort {
	QuizResponse read(Long id);
}
