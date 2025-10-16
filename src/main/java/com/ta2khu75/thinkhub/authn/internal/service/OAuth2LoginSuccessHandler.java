package com.ta2khu75.thinkhub.authn.internal.service;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ta2khu75.thinkhub.authn.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.authn.internal.model.CustomOAuth2User;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String ACCESS_TOKEN = "access_token";
	@Value("${app.frontend-url}")
	private String frontendUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
			AuthResponse auth = makeAuthResponse(oauth2User.getPrincipal());
			String accessTokenHeader = createCookie(ACCESS_TOKEN, auth.accessToken());
			String refreshTokenHeader = createCookie(REFRESH_TOKEN, auth.refreshToken());
			response.addHeader("Set-Cookie", accessTokenHeader);
			response.addHeader("Set-Cookie", refreshTokenHeader);
			response.sendRedirect(frontendUrl + "/auth/callback");
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported authentication type");
		}
	}

	private AuthResponse makeAuthResponse(UserPrincipal auth) {
		UserDto user = auth.user();
		RoleDto role = auth.role();
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		UserResponse userResponse = new UserResponse(user.id(), user.firstName(), user.lastName(), user.username(),
				user.birthday(), user.summary(), null, null, null);
		return new AuthResponse(userResponse, role.name(), accessToken, refreshToken);
	}

	private String createCookie(String name, TokenResponse token) {
		return String.format("%s=%s; ; HttpOnly; Path=/; Max-Age=%d; SameSite=Lax", name, token.getToken(),
				this.getExpiration(token.getExpiration()));
	}

	private long getExpiration(long expirationTime) {
		long remainingMillis = expirationTime - Instant.now().toEpochMilli();
		return Math.max(remainingMillis / 1000, 0);
	}

}
