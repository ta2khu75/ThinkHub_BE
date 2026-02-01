package com.ta2khu75.thinkhub.modules.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ta2khu75.thinkhub.modules.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSummary;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface UserApi extends ExistsService<Long> {
	UserSummary create(UserCreateRequest request);

	// dto
	UserSummary readSummaryByEmail(String email);

	UserSummary readSummary(String id);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);

	AuthorResponse readAuthor(Long id);

	AuthorResponse readAuthor(String id);

	List<Long> readAllUserIdByRoleId(Long id);

	long count();
}
