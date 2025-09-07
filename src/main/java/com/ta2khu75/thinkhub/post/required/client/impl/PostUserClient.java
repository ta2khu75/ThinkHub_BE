package com.ta2khu75.thinkhub.post.required.client.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.post.required.client.PostUserPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.user.api.UserApi;

@Component
public class PostUserClient extends BaseClient<UserApi> implements PostUserPort {

	protected PostUserClient(UserApi api) {
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
