package com.ta2khu75.thinkhub.authentication.internal;

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

import com.ta2khu75.thinkhub.authentication.api.AuthenticationApi;
import com.ta2khu75.thinkhub.authentication.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authentication.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.CreateAuthProviderRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.authentication.internal.config.TokenType;
import com.ta2khu75.thinkhub.authentication.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authentication.internal.model.ProviderType;
import com.ta2khu75.thinkhub.authentication.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authentication.internal.repository.AuthProviderRepository;
import com.ta2khu75.thinkhub.authentication.internal.service.JwtService;
import com.ta2khu75.thinkhub.authentication.required.port.AuthenticationUserPort;
import com.ta2khu75.thinkhub.authentication.required.port.AuthenticationRolePort;
import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.MismatchException;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;

import jakarta.mail.MessagingException;

@Service
class AuthenticationServiceImpl implements AuthenticationApi, IdDecodable {
	private final AuthProviderRepository repository;
	private final AuthenticationUserPort userPort;
	private final AuthenticationRolePort rolePort;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationServiceImpl(AuthProviderRepository repository, AuthenticationUserPort userPort,
			AuthenticationRolePort rolePort, AuthenticationManager authenticationManager, JwtService jwtService,
			RedisService redisService, PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.userPort = userPort;
		this.rolePort = rolePort;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserPrincipal account = (UserPrincipal) authentication.getPrincipal();
		return this.makeAuthResponse(account);
	}

	@Override
	public void register(RegisterRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new MismatchException("password and confirm password not matches");
		RoleResponse role = rolePort.readByName(RoleDefault.USER.name());
		UserStatusRequest status = new UserStatusRequest(false, true, role.id());
		UserRequest user = request.user();
		CreateUserRequest userRequest = new CreateUserRequest(user.firstName(), user.lastName(), request.email(),
				user.birthday(), user.summary(), status);
		UserResponse userResponse = userPort.create(userRequest);
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
		Long accountId = decodeUserId(jwt.getSubject());
		boolean exists = redisService.exists(RedisKeyBuilder.refreshToken(id));
		if (exists) {
			throw new UnauthorizedException("Refresh token is invalid or has been revoked");
		}
		redisService.setValue(RedisKeyBuilder.refreshToken(id), "", jwt.getExpiresAt());
		UserDto userDto = userPort.readDto(accountId);
		RoleDto role = rolePort.readDto(userDto.status().id());
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
		UserDto user = auth.getUser();
		RoleDto role = auth.getRole();
		TokenResponse refreshToken = jwtService.createJwt(auth, TokenType.REFRESH);
		TokenResponse accessToken = jwtService.createJwt(auth, TokenType.ACCESS);
		UserResponse userResponse = new UserResponse(user.id(), user.firstName(), user.lastName(), user.username(),
				user.birthday(), null, null, null);
		return new AuthResponse(userResponse, role.name(), accessToken, refreshToken);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public void create(CreateAuthProviderRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new MismatchException("password and confirm password not matches");
		UserResponse userResponse = userPort.create(request.user());
		AuthProvider authProvider = new AuthProvider();
		authProvider.setEmail(request.email());
		authProvider.setPassword(passwordEncoder.encode(request.password()));
		authProvider.setUserId(decodeId(userResponse.id()));
		authProvider.setProvider(ProviderType.LOCAL);
		repository.save(authProvider);
	}

	@Override
	public long count() {
		return repository.count();
	}

}
