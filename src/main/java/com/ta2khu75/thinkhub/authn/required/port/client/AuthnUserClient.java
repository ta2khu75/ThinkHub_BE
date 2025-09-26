package com.ta2khu75.thinkhub.authn.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.user.api.UserApi;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;

@Component
public class AuthnUserClient extends BaseClient<UserApi> implements AuthnUserPort {

	protected AuthnUserClient(UserApi api) {
		super(api);
	}

	@Override
	public UserDto readDto(Long id) {
		return api.readDto(id);
	}

	@Override
	public UserResponse create(CreateUserRequest request) {
		return api.create(request);
	}
}
