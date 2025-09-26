package com.ta2khu75.thinkhub.authn.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.authn.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.GoogleRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Handle user login, registration, logout and token refresh.")
@ApiController("${app.api-prefix}/authn")
public class AuthnController extends BaseController<AuthnApi> {
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String ACCESS_TOKEN = "access_token";

	protected AuthnController(AuthnApi service) {
		super(service);
	}

	@PostMapping("/register")
	@Operation(summary = "Register a new user", description = "Create a new user account with email and password. A confirmation email may be sent.")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
		service.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/change-password")
	@Operation(summary = "Change password", description = "Allow a logged-in user to change their password.")
	public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request)
			throws MessagingException {
		service.changePassword(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Authenticate the user and return an access token and refresh token.")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = service.login(request);
		ResponseCookie cookieRefresh = createCookie(REFRESH_TOKEN, response.refreshToken());
		ResponseCookie cookieAccess = createCookie(ACCESS_TOKEN, response.accessToken());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieRefresh.toString(), cookieAccess.toString())
				.body(this.makeAuthResponse(response));
	}
	@PostMapping("/google")
	public ResponseEntity<AuthResponse> authenticationWithGoogle(GoogleRequest request) throws GeneralSecurityException, IOException {
		
		AuthResponse response = service.authenticationWithGoogle(request);
		ResponseCookie cookieRefresh = createCookie(REFRESH_TOKEN, response.refreshToken());
		ResponseCookie cookieAccess = createCookie(ACCESS_TOKEN, response.accessToken());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieRefresh.toString(), cookieAccess.toString())
				.body(this.makeAuthResponse(response));
	}

	@PostMapping("/refresh-token")
	@Operation(summary = "Refresh access token", description = "Use the refresh token from cookie to get a new access token.")
	public ResponseEntity<AuthResponse> refreshToken(@CookieValue(REFRESH_TOKEN) String refreshToken) {
		AuthResponse response = service.refreshToken(refreshToken);
		ResponseCookie cookieRefresh = createCookie(REFRESH_TOKEN, response.refreshToken());
		ResponseCookie cookieAccess = createCookie(ACCESS_TOKEN, response.accessToken());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieRefresh.toString(), cookieAccess.toString())
				.body(this.makeAuthResponse(response));
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout", description = "Invalidate the user session and delete authentication tokens.")
	public ResponseEntity<Void> logout(@CookieValue(REFRESH_TOKEN) String token) {
		service.logout(token);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	private AuthResponse makeAuthResponse(AuthResponse auth) {
		return new AuthResponse(auth.user(), auth.role(), null, null);
	}

	private ResponseCookie createCookie(String name, TokenResponse toke) {
		return ResponseCookie.from(name, toke.getToken()).httpOnly(true).secure(true).sameSite("Strict")
				.maxAge(getExpiration(toke.getExpiration())).path("/").build();
	}

	private long getExpiration(long expirationTime) {
		long remainingMillis = expirationTime - Instant.now().toEpochMilli();
		return Math.max(remainingMillis / 1000, 0);
	}
	
}
