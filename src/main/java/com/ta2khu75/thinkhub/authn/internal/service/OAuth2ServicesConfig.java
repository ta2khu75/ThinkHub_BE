package com.ta2khu75.thinkhub.authn.internal.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ta2khu75.thinkhub.authn.internal.model.CustomOAuth2User;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderType;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderUser;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OAuth2ServicesConfig {
	private final OAuth2Service oauth2Service;
	private final static String ACCESS_TOKEN = "access_token";

	@Bean
	OidcUserService oidcUserService() {
		return new OidcUserService() {
			@Override
			public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
				OidcUser oidcUser = super.loadUser(userRequest);
				String registrationId = userRequest.getClientRegistration().getRegistrationId();
				Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
				attributes.put(ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
				ProviderUser providerUser = getProviderUser(registrationId, attributes);
				UserPrincipal principal = oauth2Service.authenticationWithProviderUser(providerUser);
				return new CustomOAuth2User(oidcUser, principal);
			}
		};
	}

	@Bean
	OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		return userRequest -> {
			DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
			OAuth2User oauth2User = delegate.loadUser(userRequest);
			String registrationId = userRequest.getClientRegistration().getRegistrationId();
			Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
			attributes.put("access_token", userRequest.getAccessToken().getTokenValue());
			ProviderUser providerUser = getProviderUser(registrationId, attributes);
			UserPrincipal principal = oauth2Service.authenticationWithProviderUser(providerUser);
			return new CustomOAuth2User(oauth2User, principal);
		};
	}

	private ProviderUser getProviderUserWithGoogle(Map<String, Object> attributes) {
		String userId = (String) attributes.get("sub");
		String email = (String) attributes.get("email");
		String firstName = (String) attributes.get("given_name");
		String lastName = (String) attributes.get("family_name");
		String picture = (String) attributes.get("picture");
		return new ProviderUser(userId, email, firstName, lastName, picture, ProviderType.GOOGLE);
	}

	private ProviderUser getProviderUserWithGithub(Map<String, Object> attributes) {
		String accessToken = (String) attributes.get("access_token");
		String email = (String) attributes.get("email");
		if (email == null) {
			GitHubEmailFetcher fetcher = new GitHubEmailFetcher(accessToken);
			email = fetcher.fetchPrimaryEmail();
		}
		String userId = String.valueOf(attributes.get("id"));
		String picture = (String) attributes.get("avatar_url");
		String username = (String) attributes.get("login");
		String fullName = (String) attributes.get("name");
		String firstName = "";
		String lastName = "";
		if (fullName != null) {
			String[] parts = fullName.trim().split("\\s+");
			if (parts.length > 1) {
				firstName = parts[0];
				lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
			} else {
				firstName = username;
			}
		}
		return new ProviderUser(userId, email, firstName, lastName, picture, ProviderType.GITHUB);
	}

	private ProviderUser getProviderUser(String registrationId, Map<String, Object> attributes) {
		switch (registrationId) {
		case "google": {
			return this.getProviderUserWithGoogle(attributes);
		}
		case "github": {
			return this.getProviderUserWithGithub(attributes);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + registrationId);
		}
	}
}
