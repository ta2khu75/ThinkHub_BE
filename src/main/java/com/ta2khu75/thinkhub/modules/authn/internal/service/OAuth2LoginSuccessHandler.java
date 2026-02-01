package com.ta2khu75.thinkhub.modules.authn.internal.service;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.authn.api.dto.AuthSummary;
import com.ta2khu75.thinkhub.modules.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.modules.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.modules.authn.internal.model.CustomOAuth2User;
import com.ta2khu75.thinkhub.modules.authn.internal.model.UserPrincipal;

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
			AuthSummary auth = makeAuthResponse(oauth2User.getPrincipal());
			String accessTokenHeader = createCookie(ACCESS_TOKEN, auth.accessToken());
			String refreshTokenHeader = createCookie(REFRESH_TOKEN, auth.refreshToken());
			response.addHeader("Set-Cookie", accessTokenHeader);
			response.addHeader("Set-Cookie", refreshTokenHeader);
			response.sendRedirect(frontendUrl + "/auth/callback");
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported authentication type");
		}
	}

	private AuthSummary makeAuthResponse(UserPrincipal auth) {
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		return new AuthSummary(accessToken, refreshToken);
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
