package com.ta2khu75.thinkhub.auth.internal.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.account.api.AccountApi;
import com.ta2khu75.thinkhub.account.api.dto.AccountDto;
import com.ta2khu75.thinkhub.account.api.dto.UpdatePassword;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountRequest;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.auth.api.AuthApi;
import com.ta2khu75.thinkhub.auth.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.auth.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.auth.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.auth.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.auth.internal.config.TokenType;
import com.ta2khu75.thinkhub.auth.internal.model.Auth;
import com.ta2khu75.thinkhub.authority.api.RoleApi;
import com.ta2khu75.thinkhub.authority.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authority.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.MismatchException;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthApi, IdDecodable {
	private final AccountApi accountService;
	private final RoleApi roleService;
	private final AuthenticationManager authenticationManager;
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
	public void register(RegisterRequest request) {
		RoleResponse role = roleService.readByName(RoleDefault.USER.name());
		AccountStatusRequest status = new AccountStatusRequest(false, true, role.id());
		AccountRequest accountRequest = new AccountRequest(request.username(), request.password(),
				request.confirmPassword(), request.email(), request.profile(), status);
		accountService.create(accountRequest);
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword()))
			throw new MismatchException("New password and confirm password not matches");
		accountService.updatePassword(new UpdatePassword(request.password(), request.newPassword()));
	}

	@Override
	public AuthResponse refreshToken(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		Long accountId = decodeAccountId(jwt.getSubject());
		boolean exists = redisService.exists(RedisKeyBuilder.refreshToken(id));
		if (exists) {
			throw new UnauthorizedException("Refresh token is invalid or has been revoked");
		}
		redisService.setValue(RedisKeyBuilder.refreshToken(id), "", jwt.getExpiresAt());
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
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		return new AuthResponse(account.id(), account.profile(), role.name(), accessToken, refreshToken);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.ACCOUNT;
	}
}
