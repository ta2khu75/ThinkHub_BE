package com.ta2khu75.thinkhub.modules.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ta2khu75.thinkhub.modules.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSummary;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface UserApi extends SearchService<UserSearch, UserResponse>, ExistsService<Long> {

	UserResponse readMe();

	UserResponse updateMe(UserRequest request);

	UserResponse update(String userId, UserRequest request);

	UserResponse read(String userId);

	UserStatusResponse updateStatus(String userId, UserStatusRequest request);

	void delete(String id);

	// summary
	UserSummary create(UserCreateRequest request);

	UserSummary readSummaryByEmail(String email);

	UserSummary readSummary(Long id);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);

	AuthorResponse readAuthor(Long id);

	AuthorResponse readAuthor(String id);

	List<Long> readAllUserIdByRoleId(Long id);

	long count();

}
