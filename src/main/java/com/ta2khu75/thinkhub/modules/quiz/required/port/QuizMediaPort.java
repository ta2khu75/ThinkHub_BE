package com.ta2khu75.thinkhub.modules.quiz.required.port;

import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;

public interface QuizMediaPort {
	MediaResponse read(Long id);
}
