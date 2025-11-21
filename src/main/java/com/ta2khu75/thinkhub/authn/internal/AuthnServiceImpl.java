package com.ta2khu75.thinkhub.authn.internal;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;
import com.ta2khu75.thinkhub.authn.api.AuthnApi;
import com.ta2khu75.thinkhub.authn.api.dto.AuthSummary;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.internal.service.JwtService;
import com.ta2khu75.thinkhub.authn.internal.util.PasswordUtil;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthProviderPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.exception.MismatchException;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusSummary;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AuthnServiceImpl implements AuthnApi, IdDecodable {

	private final AuthenticationManager authenticationManager;
	private final AuthnAuthProviderPort authProviderPort;
	private final AuthnUserPort userPort;
	private final AuthnAuthzPort authzPort;
	private final JwtService jwtService;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AuthSummary login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return this.makeAuthResponse(userPrincipal);
	}

	@Override
	public void register(RegisterRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new MismatchException("password and confirm password not matches");
		RoleResponse role = authzPort.readByName(RoleDefault.USER.name());
		UserStatusRequest status = new UserStatusRequest(false, true, role.id());
		UserRequest user = request.user();
		UserCreateRequest userCreate = new UserCreateRequest(request.email(), user.firstName(), user.lastName(), null,
				status);
		String password = passwordEncoder.encode(request.password());
		this.createAuth(userCreate, password);
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword()))
			throw new MismatchException("New password and confirm password not matches");
		if (request.newPassword().equals(request.password()))
			throw new InvalidDataException("Current password and new password are same");
		AuthProviderSummary authProvider = authProviderPort.readByEmailAndProvider(SecurityUtil.getCurrentUserId(),
				ProviderType.LOCAL);
		if (!passwordEncoder.matches(request.password(), authProvider.password()))
			throw new MismatchException("Password not matches");
		authProviderPort.updatePassword(authProvider.id(), request.password());
	}

	@Override
	public AuthSummary refreshToken(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		String userId = jwt.getSubject();
		boolean exists = redisService.exists(RedisKeyBuilder.refreshToken(id));
		if (exists) {
			throw new UnauthorizedException("Refresh token is invalid or has been revoked");
		}
		redisService.setValue(RedisKeyBuilder.refreshToken(id), "", jwt.getExpiresAt());
		UserSummary userDto = userPort.readSummary(userId);
		RoleSummary role = authzPort.readSummary(userDto.status().id());
		return this.makeAuthResponse(new UserPrincipal(userDto, role, null));
	}

	@Override
	public void logout(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		Instant expiresAt = jwt.getExpiresAt();
		Duration ttl = Duration.between(Instant.now(), expiresAt);
		redisService.setValue(RedisKeyBuilder.refreshToken(id), String.class, ttl);

	}

	private AuthSummary makeAuthResponse(UserPrincipal auth) {
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		return new AuthSummary(accessToken, refreshToken);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public void create(UserCreateRequest request, String password) {
		String passwordEncode = passwordEncoder.encode(password);
		createAuth(request, passwordEncode);
	}

	@Override
	public void createGeneratorPassword(UserCreateRequest request) {
		String password = passwordEncoder.encode(PasswordUtil.generate(8));
		createAuth(request, password);
	}

	private void createAuth(UserCreateRequest request, String password) {
		UserSummary user = userPort.create(request);
		AuthProviderLocal authProvider = new AuthProviderLocal(user.email(), password, user.id());
		authProviderPort.create(authProvider);
	}

}
