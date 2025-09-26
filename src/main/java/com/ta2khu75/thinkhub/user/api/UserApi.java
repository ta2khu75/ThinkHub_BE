package com.ta2khu75.thinkhub.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;

public interface UserApi extends SearchService<UserSearch, UserResponse>, ExistsService<Long> {
	UserResponse create(CreateUserRequest request);

	UserResponse update(Long userId, UserRequest request);

	UserResponse read(Long userId);

	UserResponse readByEmail(String email);

	void delete(Long id);

	// dto
	UserDto readDtoByEmail(String email);

	UserDto readDtoByUsername(String username);

	UserDto readDto(Long id);

	// status
	UserStatusResponse updateStatus(Long accountId, UserStatusRequest request);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);

	AuthorResponse readAuthor(Long id);
	
	List<Long> readUserIdsByRoleName(String roleName);

}
