package com.ta2khu75.thinkhub.modules.quiz.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.media.api.MediaApi;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.modules.quiz.required.port.QuizMediaPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
public class QuizMediaClient extends BaseClient<MediaApi> implements QuizMediaPort {

	protected QuizMediaClient(MediaApi api) {
		super(api);
	}

	@Override
	public MediaResponse read(Long id) {
		return api.read(id);
	}

}
