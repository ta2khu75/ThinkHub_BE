package com.ta2khu75.thinkhub.authn.internal.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authn.api.dto.TokenResponse;
import com.ta2khu75.thinkhub.authn.internal.config.JwtProperties;
import com.ta2khu75.thinkhub.authn.internal.config.JwtProviderFactory;
import com.ta2khu75.thinkhub.authn.internal.config.TokenConfig;
import com.ta2khu75.thinkhub.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.authn.internal.model.UserPrincipal;
import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
	private final JwtProviderFactory jwtProviderFactory;
	private final JwtProperties jwtProperties;

	public TokenResponse createJwt(UserPrincipal auth, TokenType tokenType) {
		UserDto user= auth.user();
		RoleDto role = auth.role();
		Instant now = Instant.now();
		TokenConfig tokenConfig = jwtProperties.getConfigByType(tokenType);
		JwtEncoder jwtEncoder = jwtProviderFactory.getEncoder(tokenType);
		Instant validity = now.plus(tokenConfig.expiration(), ChronoUnit.SECONDS);
		JwsHeader jwsHeader = JwsHeader.with(JwtProviderFactory.JWT_ALGORITHM).build();
		JwtClaimsSet claims;
		switch (tokenType) {
		case ACCESS: {
			claims = JwtClaimsSet.builder().issuer("com.ta2khu75").issuedAt(now).expiresAt(validity)
					.subject(user.id()).claim("username", user.username()).claim("scope", "ROLE_" + role.name())
					.build();
			break;
		}
		case REFRESH: {
			claims = JwtClaimsSet.builder().id(UUID.randomUUID().toString()).issuer("com.ta2khu75").issuedAt(now)
					.expiresAt(validity).subject(user.id()).build();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + tokenType);
		}
		String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
		return new TokenResponse(token, validity.toEpochMilli());
	}

	public Jwt validateToken(String token, TokenType type) {
		JwtDecoder jwtDecoder = jwtProviderFactory.getDecoder(type);
		return jwtDecoder.decode(token);
	}

	public <T> T getClaim(Jwt jwt, String claimName, Class<T> clazz) {
		T claim = jwt.getClaim(claimName);
		if (claim == null) {
			throw new UnauthorizedException("Missing claim: " + claimName);
		}
		return claim;
	}
}
