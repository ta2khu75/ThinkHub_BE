package com.ta2khu75.thinkhub.authn.internal.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;

public record UserPrincipal(UserDto user, RoleDto role, AuthProvider provider) implements UserDetails, Serializable {

	private static final long serialVersionUID = 7209939028389672571L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return provider.getPassword();
	}

	@Override
	public String getUsername() {
		return user.email();
	}

	@Override
	public boolean isEnabled() {
		return user.status().enabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.status().nonLocked();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
