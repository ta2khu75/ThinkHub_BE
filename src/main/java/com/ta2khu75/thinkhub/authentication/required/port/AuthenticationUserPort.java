package com.ta2khu75.thinkhub.authentication.required.port;

import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;

public interface AuthenticationUserPort {
	UserResponse create(CreateUserRequest request);

	UserDto readDto(Long id);
}
