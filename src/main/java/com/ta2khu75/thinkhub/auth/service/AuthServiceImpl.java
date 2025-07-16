package com.ta2khu75.thinkhub.auth.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.auth.AuthResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.LoginRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.auth.TokenResponse;
import com.ta2khu75.thinkhub.auth.model.Auth;
import com.ta2khu75.thinkhub.authority.RoleDto;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, IdDecodable {
	private final AccountService accountService;
	private final RoleService roleService;
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
		accountService.changePassword(request);
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
		TokenResponse refreshToken = jwtService.createRefreshToken(account);
		String accessToken = jwtService.createAccessToken(account, role);
		return new AuthResponse(account.id(), account.profile(), role.name(), accessToken, refreshToken);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.ACCOUNT;
	}
}
