package com.ta2khu75.thinkhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private TokenConfig access;
	private TokenConfig refresh;

	public TokenConfig getConfigByType(TokenType type) {
		return switch (type) {
		case ACCESS -> access;
		case REFRESH -> refresh;
		};
	}

	public String getSecretByType(TokenType type) {
		return getConfigByType(type).secret();
	}

	public long getExpirationByType(TokenType type) {
		return getConfigByType(type).expiration();
	}

	public enum TokenType {
		ACCESS, REFRESH;
	}

	public record TokenConfig(String secret, long expiration) {
	}
}
