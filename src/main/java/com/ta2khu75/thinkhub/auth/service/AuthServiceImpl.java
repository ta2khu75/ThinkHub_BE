package com.ta2khu75.thinkhub.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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
import com.ta2khu75.thinkhub.shared.exception.NotMatchesException;
import com.ta2khu75.thinkhub.shared.exception.UnAuthenticatedException;
import com.ta2khu75.thinkhub.shared.service.JwtService;
import com.ta2khu75.thinkhub.shared.service.RedisService;
import com.ta2khu75.thinkhub.shared.service.RedisService.RedisKeyBuilder;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AccountService accountService;
	private final RoleService roleService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RedisService redisService;

	@Override
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Auth account = (Auth) authentication.getPrincipal();
		return this.makeAuthResponse(account);
	}

	@Override
	public void register(RegisterRequest request) throws MessagingException {

	}

	@Override
	public void changePassword(ChangePasswordRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public AuthResponse refreshToken(String token) {
		Jwt jwt = jwtService.validateToken(token, TokenType.REFRESH);
		String id = jwt.getId().toString();
		String accountId = jwt.getSubject();
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

//	@Override
//	public void changePassword(ChangePasswordRequest request) {
//		if (!request.newPassword().equals(request.confirmPassword()))
//			throw new NotMatchesException("New password and confirm password not matches");
//		AccountDto account = account
//		if (!passwordEncoder.matches(request.getPassword(), account.getPassword()))
//			throw new NotMatchesException("Password not matches");
//		account.setPassword(passwordEncoder.encode(request.getNewPassword()));
//		account = repository.save(account);
//	}
//
//	@Override
//	public void register(AccountRequest request) throws MessagingException {
//		if (!request.getPassword().equals(request.getConfirmPassword()))
//			throw new NotMatchesException("password and confirm password not matches");
//		if (repository.existsByEmail(request.getEmail()))
//			throw new ExistingException("Email already exists");
//		AccountProfile profile = mapper.toEntity(request.getProfile());
//		profile.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
//		Role role = FunctionUtil.findOrThrow(RoleDefault.USER.name(), Role.class, roleRepository::findByName);
//		AccountStatus status = new AccountStatus();
//		status.setRole(role);
//		status.setCodeVerify(UUID.randomUUID().toString());
//
//		Account account = new Account();
//		account.setEmail(request.getEmail().toLowerCase());
//		account.setPassword(passwordEncoder.encode(request.getPassword()));
//		account.setProfile(profile);
//		account.setStatus(status);
//		try {
//			account = repository.save(account);
//		} catch (DataIntegrityViolationException e) {
//			throw new ExistingException("Email already exists");
//		}
//		sendMailScheduling.addMail(account.getEmail(), "Confirm your email",
//				EmailTemplateUtil.getVerify(status.getCodeVerify()), true);
//	}

	private AuthResponse makeAuthResponse(Auth auth) {
		AccountDto account = auth.getAccount();
		RoleDto role = auth.getRole();
		TokenResponse refreshToken = jwtService.createRefreshToken(account);
		String accessToken = jwtService.createAccessToken(account, role);
		return new AuthResponse(account.profile(), role.name(), accessToken, refreshToken);
	}
}
