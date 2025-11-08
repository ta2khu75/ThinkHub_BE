package com.ta2khu75.thinkhub.authn.internal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authn.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderUser;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.internal.repository.AuthProviderRepository;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusSummary;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements IdDecodable {

	private final AuthnUserPort userPort;
	private final AuthnAuthzPort authzPort;
	private final AuthProviderRepository repository;

	public UserPrincipal authenticationWithProviderUser(ProviderUser providerUser) {
		UserSummary user;
		try {
			user = userPort.readSummaryByEmail(providerUser.email());
		} catch (Exception e) {
			RoleResponse role = authzPort.readByName(RoleDefault.USER.name());
			UserStatusSummary status = new UserStatusSummary(null, true, true, role.id());
			UserSummary userRequest = new UserSummary(null, providerUser.firstName(), providerUser.lastName(),
					providerUser.email(), null, status);

			user = userPort.create(userRequest);
		}
		RoleSummary role = authzPort.readSummary(user.status().roleId());
		Optional<AuthProvider> authProviderOptional = repository.findByEmailAndProvider(providerUser.email(),
				providerUser.provider());
		AuthProvider authProvider;
		if (authProviderOptional.isPresent()) {
			authProvider = authProviderOptional.get();
		} else {
			AuthProvider auth = new AuthProvider();
			auth.setProviderUserId(providerUser.userId());
			auth.setEmail(providerUser.email());
			auth.setProvider(providerUser.provider());
			auth.setUserId(this.decodeId(user.id()));
			authProvider = repository.save(auth);
		}
		return new UserPrincipal(user, role, authProvider);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

}
