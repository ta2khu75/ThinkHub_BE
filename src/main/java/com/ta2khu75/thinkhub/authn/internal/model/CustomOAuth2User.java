package com.ta2khu75.thinkhub.authn.internal.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OidcUser {

	private final OAuth2User oauth2User;
	private final OidcUser oidcUser; // null nếu provider không phải OIDC
	private final UserPrincipal principal;

	public CustomOAuth2User(OAuth2User oauth2User, UserPrincipal principal) {
		this.oauth2User = oauth2User;
		this.principal = principal;
		this.oidcUser = (oauth2User instanceof OidcUser oidc) ? oidc : null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2User.getName();
	}

	@Override
	public Map<String, Object> getClaims() {
		return oidcUser != null ? oidcUser.getClaims() : Map.of();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return oidcUser != null ? oidcUser.getUserInfo() : null;
	}

	@Override
	public OidcIdToken getIdToken() {
		return oidcUser != null ? oidcUser.getIdToken() : null;
	}

	public UserPrincipal getPrincipal() {
		return principal;
	}

	public boolean isOidc() {
		return oidcUser != null;
	}
}
