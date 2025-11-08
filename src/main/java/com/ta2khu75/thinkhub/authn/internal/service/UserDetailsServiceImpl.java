package com.ta2khu75.thinkhub.authn.internal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authn.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderType;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.internal.repository.AuthProviderRepository;
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
	AuthProviderRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AuthProvider authProvider = repository.findByEmailAndProvider(username, ProviderType.LOCAL).orElseThrow();
			UserSummary user = userPort.readSummary(authProvider.getUserId());
			RoleSummary role = authzPort.readSummary(user.status().roleId());
			return new UserPrincipal(user, role, authProvider);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Bad credentials");
		}
	}
}
