package com.ta2khu75.thinkhub.authn.internal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthProviderPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {
	AuthnUserPort userPort;
	AuthnAuthzPort authzPort;
	AuthnAuthProviderPort authProviderPort;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AuthProviderSummary authProvider = authProviderPort.readByEmailAndProvider(username, ProviderType.LOCAL);
			UserSummary user = userPort.readSummary(authProvider.userId());
			RoleSummary role = authzPort.readSummary(user.status().roleId());
			return new UserPrincipal(user, role, authProvider);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Bad credentials");
		}
	}
}
