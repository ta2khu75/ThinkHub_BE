package com.ta2khu75.thinkhub.authn.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.user.api.UserApi;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

@Component
public class AuthnUserClient extends BaseClient<UserApi> implements AuthnUserPort {

	protected AuthnUserClient(UserApi api) {
		super(api);
	}

	@Override
	public UserSummary readSummary(Long id) {
		return api.readSummary(id);
	}

	@Override
	public UserSummary readSummary(String id) {
		return api.readSummary(id);
	}

	@Override
	public UserSummary create(UserSummary request) {
		return api.create(request);
	}

	@Override
	public UserResponse readByEmail(String email) {
		return api.readByEmail(email);
	}

	@Override
	public UserSummary readSummaryByEmail(String email) {
		return api.readSummaryByEmail(email);
	}

}
