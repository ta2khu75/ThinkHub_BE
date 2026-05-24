package com.ta2khu75.thinkhub.modules.authn.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.authProvider.api.AuthProviderApi;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.modules.authProvider.api.model.ProviderType;
import com.ta2khu75.thinkhub.modules.authn.required.port.AuthnAuthProviderPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
class AuthnAuthProviderClient extends BaseClient<AuthProviderApi> implements AuthnAuthProviderPort {

	protected AuthnAuthProviderClient(AuthProviderApi api) {
		super(api);
	}

	@Override
	public AuthProviderSummary create(AuthProviderLocal local) {
		return api.create(local);
	}

	@Override
	public AuthProviderSummary create(AuthProviderOAuth2 oAuth2) {
		return api.create(oAuth2);
	}

	@Override
	public AuthProviderSummary readByEmailAndProvider(String email, ProviderType provider) {
		return api.readByEmailAndProvider(email, provider);
	}

	@Override
	public void updatePassword(Long id, String password) {
		api.updatePassword(id, password);
	}

	@Override
	public AuthProviderSummary readByUserIdAndProvider(Long userId, ProviderType provider) {
		return api.readByUserIdAndProvider(userId, provider);
	}

}
