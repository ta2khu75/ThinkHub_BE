package com.ta2khu75.thinkhub.user.internal.service;

import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;

public interface UserService extends SearchService<UserSearch, UserResponse> {

	UserResponse readMe();

	UserResponse updateMe(UserRequest request);

	UserResponse update(String userId, UserRequest request);

	UserResponse read(String userId);

	UserStatusResponse updateStatus(String userId, UserStatusRequest request);

	void delete(String id);

}
