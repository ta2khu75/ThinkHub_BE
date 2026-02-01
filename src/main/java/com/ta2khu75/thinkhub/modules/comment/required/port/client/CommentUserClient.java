package com.ta2khu75.thinkhub.modules.comment.required.port.client;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.comment.required.port.CommentUserPort;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
@Component
public class CommentUserClient extends BaseClient<UserApi> implements CommentUserPort {

	protected CommentUserClient(UserApi api) {
		super(api);
	}

	@Override
	public AuthorResponse readAuthor(String id) {
		return api.readAuthor(id);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds) {
		return api.readMapAuthorsByUserIds(userIds);
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		return api.readAuthor(id);
	}

}
