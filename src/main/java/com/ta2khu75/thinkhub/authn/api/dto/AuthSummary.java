package com.ta2khu75.thinkhub.authn.api.dto;

public record AuthSummary(TokenResponse accessToken, TokenResponse refreshToken) {
}
