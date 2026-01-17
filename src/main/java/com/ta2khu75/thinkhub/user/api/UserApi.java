package com.ta2khu75.thinkhub.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

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
