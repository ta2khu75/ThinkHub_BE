package com.ta2khu75.thinkhub.auth.controller;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.auth.AuthResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.LoginRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.auth.TokenResponse;
import com.ta2khu75.thinkhub.auth.service.AuthService;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Handle user login, registration, logout and token refresh.")
@ApiController("${app.api-prefix}/auth")
public class AuthController extends BaseController<AuthService> {
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String ACCESS_TOKEN = "access_token";

	protected AuthController(AuthService service) {
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
		return new AuthResponse(auth.id(), auth.profile(), auth.role(), null, null);
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
