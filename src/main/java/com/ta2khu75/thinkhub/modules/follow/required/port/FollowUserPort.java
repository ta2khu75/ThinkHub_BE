package com.ta2khu75.thinkhub.modules.follow.required.port;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

public interface FollowUserPort {
	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);
}
