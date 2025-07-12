package com.ta2khu75.thinkhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "id")
public class IdProperties {
	private IdConfig quiz;
	private IdConfig post;
	private IdConfig account;

	public IdConfig getConfigByType(IdType type) {
		return switch (type) {
		case QUIZ -> quiz;
		case POST -> post;
		case ACCOUNT -> account;
		};
	}

	public enum IdType {
		QUIZ, ACCOUNT, POST
	}

	public record IdConfig(int salt, int offset, String alphabet) {
	}
}
