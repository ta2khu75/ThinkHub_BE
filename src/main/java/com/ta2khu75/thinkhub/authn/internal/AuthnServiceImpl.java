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

import com.ta2khu75.thinkhub.authn.api.AuthnApi;
import com.ta2khu75.thinkhub.authn.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.authn.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authn.internal.model.ProviderType;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authn.internal.repository.AuthProviderRepository;
import com.ta2khu75.thinkhub.authn.internal.service.JwtService;
import com.ta2khu75.thinkhub.authn.internal.util.PasswordGenerator;
import com.ta2khu75.thinkhub.authn.required.port.AuthnAuthzPort;
import com.ta2khu75.thinkhub.authn.required.port.AuthnUserPort;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.MismatchException;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusSummary;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AuthnServiceImpl implements AuthnApi, IdDecodable {
	private final AuthProviderRepository repository;
	private final AuthnUserPort userPort;
	private final AuthnAuthzPort authzPort;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AuthResponse login(LoginRequest request) {
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
		UserStatusSummary status = new UserStatusSummary(null, false, true, role.id());
		UserRequest user = request.user();
		UserSummary userSummary = new UserSummary(null, user.firstName(), user.lastName(), request.email(), null,
				status);
		UserSummary userResponse = userPort.create(userSummary);
		AuthProvider authProvider = new AuthProvider();
		authProvider.setEmail(request.email());
		authProvider.setPassword(passwordEncoder.encode(request.password()));
		authProvider.setUserId(decodeId(userResponse.id()));
		authProvider.setProvider(ProviderType.LOCAL);
		repository.save(authProvider);
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword()))
			throw new MismatchException("New password and confirm password not matches");
		AuthProvider authProvider = repository
				.findByUserIdAndProvider(SecurityUtil.getCurrentUserIdDecode(), ProviderType.LOCAL)
				.orElseThrow(() -> new UnauthorizedException("User not found"));
		if (!passwordEncoder.matches(request.password(), authProvider.getPassword()))
			throw new MismatchException("Password not matches");
		authProvider.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(authProvider);
	}

	@Override
	public AuthResponse refreshToken(String token) {
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

	private AuthResponse makeAuthResponse(UserPrincipal auth) {
		UserSummary user = auth.user();
		RoleSummary role = auth.role();
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		UserResponse userResponse = new UserResponse(user.id(), user.firstName(), user.lastName(), user.username());
		return new AuthResponse(userResponse, role.name(), accessToken, refreshToken);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public void create(UserSummary user) {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setEmail(user.email());
		authProvider.setPassword(passwordEncoder.encode(PasswordGenerator.generate(12)));
		authProvider.setUserId(decodeId(user.id()));
		authProvider.setProvider(ProviderType.LOCAL);
		repository.save(authProvider);
	}

	@Override
	public void create(UserSummary user, String password) {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setEmail(user.email());
		authProvider.setPassword(passwordEncoder.encode(password));
		authProvider.setUserId(decodeId(user.id()));
		authProvider.setProvider(ProviderType.LOCAL);
		repository.save(authProvider);
	}

}
