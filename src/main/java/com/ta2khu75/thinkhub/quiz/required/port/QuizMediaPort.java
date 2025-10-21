package com.ta2khu75.thinkhub.quiz.required.port;

import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;

public interface QuizMediaPort {
	MediaResponse read(Long id);
}
