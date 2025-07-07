package com.ta2khu75.thinkhub.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenType;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtProviderFactory {
	private final JwtProperties jwtProperties;

	public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

	public JwtEncoder getEncoder(TokenType type) {
		String secret = jwtProperties.getSecretByType(type);
		return new NimbusJwtEncoder(new ImmutableSecret<>(decodeSecret(secret)));
	}

	public JwtDecoder getDecoder(TokenType type) {
		String secret = jwtProperties.getSecretByType(type);
		return NimbusJwtDecoder.withSecretKey(decodeSecret(secret)).macAlgorithm(MacAlgorithm.HS512).build();
	}

	private SecretKey decodeSecret(String base64Secret) {
		byte[] keyBytes = Base64.from(base64Secret).decode();
		return new SecretKeySpec(keyBytes, MacAlgorithm.HS512.getName());
	}
}
