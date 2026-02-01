package com.ta2khu75.thinkhub.modules.authn.api.dto;

public record AuthSummary(TokenResponse accessToken, TokenResponse refreshToken) {
}
