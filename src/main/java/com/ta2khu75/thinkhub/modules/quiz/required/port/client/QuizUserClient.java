package com.ta2khu75.thinkhub.modules.quiz.required.port.client;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.quiz.required.port.QuizUserPort;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

@Component
public class QuizUserClient extends BaseClient<UserApi> implements QuizUserPort {

	protected QuizUserClient(UserApi api) {
		super(api);
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		return api.readAuthor(id);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> ids) {
		return api.readMapAuthorsByUserIds(ids);
	}

}
