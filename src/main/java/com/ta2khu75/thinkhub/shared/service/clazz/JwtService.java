package com.ta2khu75.thinkhub.shared.service.clazz;

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

import com.ta2khu75.thinkhub.account.dto.AccountDto;
import com.ta2khu75.thinkhub.auth.TokenResponse;
import com.ta2khu75.thinkhub.authority.dto.RoleDto;
import com.ta2khu75.thinkhub.config.JwtProperties;
import com.ta2khu75.thinkhub.config.JwtProviderFactory;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenConfig;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenType;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
	private final JwtProviderFactory jwtProviderFactory;
	private final JwtProperties jwtProperties;

	public String createAccessToken(AccountDto account, RoleDto role) {
		Instant now = Instant.now();
		TokenConfig tokenConfig = jwtProperties.getConfigByType(TokenType.ACCESS);
		JwtEncoder jwtEncoder = jwtProviderFactory.getEncoder(TokenType.ACCESS);
		Instant validity = now.plus(tokenConfig.expiration(), ChronoUnit.SECONDS);
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("com.ta2khu75").issuedAt(now).expiresAt(validity)
				.subject(account.id()).claim("username", account.username()).claim("scope", "ROLE_" + role.name())
				.build();
		JwsHeader jwsHeader = JwsHeader.with(JwtProviderFactory.JWT_ALGORITHM).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}

	public TokenResponse createRefreshToken(AccountDto account) {
		Instant now = Instant.now();
		TokenConfig tokenConfig = jwtProperties.getConfigByType(TokenType.REFRESH);
		JwtEncoder jwtEncoder = jwtProviderFactory.getEncoder(TokenType.REFRESH);
		Instant validity = now.plus(tokenConfig.expiration(), ChronoUnit.SECONDS);
		JwtClaimsSet claims = JwtClaimsSet.builder().id(UUID.randomUUID().toString()).issuer("com.ta2khu75")
				.issuedAt(now).expiresAt(validity).subject(account.id()).build();
		JwsHeader jwsHeader = JwsHeader.with(JwtProviderFactory.JWT_ALGORITHM).build();
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
