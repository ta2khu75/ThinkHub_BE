package com.ta2khu75.thinkhub.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

public interface UserApi extends SearchService<UserSearch, UserResponse>, ExistsService<Long> {
	UserSummary create(UserSummary user);

	UserResponse create(UserCreateRequest request);

	UserResponse update(String userId, UserRequest request);

	UserResponse read(String userId);

	UserResponse readByEmail(String email);

	void delete(String id);

	// dto
	UserSummary readSummaryByEmail(String email);

	UserSummary readDtoByUsername(String username);

	UserSummary readSummary(String id);

	UserSummary readSummary(Long id);

	// status
	UserStatusResponse updateStatus(String userId, UserStatusRequest request);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);

	AuthorResponse readAuthor(Long id);

	AuthorResponse readAuthor(String id);

	List<Long> readAllUserIdByRoleId(Long id);

	long count();
}
