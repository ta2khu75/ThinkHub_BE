package com.ta2khu75.thinkhub.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.authority.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Auth implements UserDetails {
	private static final long serialVersionUID = 7209939028389672571L;
	AccountDto account;
	RoleDto role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.name())));
//		authorities.addAll(role.permissions().stream()
//				.map(permission -> new SimpleGrantedAuthority(permission.summary())).toList());
		return authorities;
	}

	@Override
	public String getPassword() {
		return account.password();
	}

	@Override
	public String getUsername() {
		return account.username();
	}
}
