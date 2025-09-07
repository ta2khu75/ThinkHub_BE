package com.ta2khu75.thinkhub.authentication.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 7209939028389672571L;
	UserDto user;
	RoleDto role;
	AuthProvider provider;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.name())));
		return authorities;
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
}
