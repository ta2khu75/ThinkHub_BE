package com.ta2khu75.thinkhub.authn.internal.service;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderUser;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthProviderPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.domain.enums.RoleDefault;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
	AuthnUserPort userPort;
	AuthnAuthProviderPort authProviderPort;
	AuthnAuthzPort authzPort;

	private UserSummary getUser(ProviderUser providerUser) {
		UserSummary user;
		try {
			user = userPort.readSummaryByEmail(providerUser.email());
		} catch (Exception e) {
			RoleResponse role = authzPort.readByName(RoleDefault.USER.name());
			UserStatusRequest status = new UserStatusRequest(true, true, role.id());
			UserCreateRequest userCreate = new UserCreateRequest(providerUser.email(), providerUser.firstName(),
					providerUser.lastName(), null, status);
			user = userPort.create(userCreate);
		}
		return user;
	}

	private AuthProviderSummary getAuthProvider(ProviderUser providerUser, UserSummary user) {
		AuthProviderSummary authProvider;
		try {
			authProvider = authProviderPort.readByEmailAndProvider(providerUser.email(), providerUser.provider());
		} catch (Exception e) {
			AuthProviderOAuth2 auth = new AuthProviderOAuth2(providerUser.provider(), providerUser.userId(),
					providerUser.email(), user.id());
			authProvider = authProviderPort.create(auth);
		}
		return authProvider;
	}

	public UserPrincipal authenticationWithProviderUser(ProviderUser providerUser) {
		UserSummary user = getUser(providerUser);
		RoleSummary role = authzPort.readSummary(user.status().roleId());
		AuthProviderSummary authProvider = getAuthProvider(providerUser, user);
		return new UserPrincipal(user, role, authProvider);
	}

}
