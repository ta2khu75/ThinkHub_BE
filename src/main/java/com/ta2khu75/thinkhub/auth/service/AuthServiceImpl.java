package com.ta2khu75.thinkhub.auth.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.auth.AuthResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.LoginRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.auth.TokenResponse;
import com.ta2khu75.thinkhub.auth.model.Auth;
import com.ta2khu75.thinkhub.authority.RoleDto;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenType;
import com.ta2khu75.thinkhub.shared.exception.UnAuthenticatedException;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AccountService accountService;
	private final RoleService roleService;
	private final AuthenticationManager authenticationManager;
	private final ApplicationEventPublisher events;
	private final JwtService jwtService;
	private final RedisService redisService;

	@Override
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.identifier().toLowerCase(), request.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Auth account = (Auth) authentication.getPrincipal();
		return this.makeAuthResponse(account);
	}

	@Override
	@Transactional
	public void register(RegisterRequest request) {
		events.publishEvent(request);
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest request) {
		events.publishEvent(request);
	}

	@Override
	public AuthResponse refreshToken(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		Long accountId = Long.valueOf(jwt.getSubject());
		boolean exists = redisService.exists(RedisKeyBuilder.refreshToken(id));
		if (exists) {
			throw new UnAuthenticatedException("");
		}
		AccountDto account = accountService.readDto(accountId);
		RoleDto role = roleService.readDto(account.status().id());
		return this.makeAuthResponse(new Auth(account, role));
	}

	@Override
	public void logout(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		Instant expiresAt = jwt.getExpiresAt();
		Duration ttl = Duration.between(Instant.now(), expiresAt);
		redisService.setValue(RedisKeyBuilder.refreshToken(id), String.class, ttl);

	}

	private AuthResponse makeAuthResponse(Auth auth) {
		AccountDto account = auth.getAccount();
		RoleDto role = auth.getRole();
		TokenResponse refreshToken = jwtService.createRefreshToken(account);
		String accessToken = jwtService.createAccessToken(account, role);
		return new AuthResponse(account.profile(), role.name(), accessToken, refreshToken);
	}
}
