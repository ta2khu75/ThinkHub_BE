package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;

public interface AuthnUserPort {
	UserResponse create(CreateUserRequest request);

	UserResponse readByEmail(String email);

	UserDto createDto(CreateUserRequest request);

	UserDto readDto(Long id);

	UserDto readDtoByEmail(String email);
}
