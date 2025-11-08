package com.ta2khu75.thinkhub.user.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.authn.api.AuthnApi;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;
import com.ta2khu75.thinkhub.user.required.port.UserAuthnPort;

@Component
public class UserAuthnClient extends BaseClient<AuthnApi> implements UserAuthnPort {

	protected UserAuthnClient(AuthnApi api) {
		super(api);
	}

	@Override
	public void create(UserSummary user) {
		api.create(user);
	}

}
