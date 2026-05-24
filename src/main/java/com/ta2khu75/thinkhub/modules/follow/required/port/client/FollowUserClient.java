package com.ta2khu75.thinkhub.modules.follow.required.port.client;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.follow.required.port.FollowUserPort;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

@Component
public class FollowUserClient extends BaseClient<UserApi> implements FollowUserPort {

	protected FollowUserClient(UserApi api) {
		super(api);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds) {
		return api.readMapAuthorsByUserIds(userIds);
	}

}
