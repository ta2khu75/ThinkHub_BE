package com.ta2khu75.thinkhub.authentication.internal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authentication.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authentication.internal.model.ProviderType;
import com.ta2khu75.thinkhub.authentication.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authentication.internal.repository.AuthProviderRepository;
import com.ta2khu75.thinkhub.authentication.required.port.AuthenticationRolePort;
import com.ta2khu75.thinkhub.authentication.required.port.AuthenticationUserPort;
import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {
	AuthenticationUserPort userPort;
	AuthenticationRolePort rolePort;
	AuthProviderRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AuthProvider authProvider = repository.findByEmailAndProvider(username, ProviderType.LOCAL).orElseThrow();
			UserDto user = userPort.readDto(authProvider.getUserId());
			RoleDto role = rolePort.readDto(user.status().roleId());
			return new UserPrincipal(user, role, authProvider);
		} catch (Exception e) {
			throw new UsernameNotFoundException("Bad credentials");
		}
	}
}
