package com.ta2khu75.thinkhub.authn.required.port;

import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;

public interface AuthnAuthProviderPort {
	AuthProviderSummary create(AuthProviderLocal local);

	AuthProviderSummary create(AuthProviderOAuth2 oAuth2);

	AuthProviderSummary readByEmailAndProvider(String email, ProviderType provider);

	AuthProviderSummary readByUserIdAndProvider(String userId, ProviderType provider);

	void updatePassword(Long id, String password);
}
