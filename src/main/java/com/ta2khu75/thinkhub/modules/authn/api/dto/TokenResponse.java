package com.ta2khu75.thinkhub.modules.authn.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
	private String token;
	private long expiration;
}
