package com.ta2khu75.thinkhub.authentication.api.dto;

import com.ta2khu75.thinkhub.user.api.dto.UserResponse;

public record AuthResponse(UserResponse user, String role, TokenResponse accessToken, TokenResponse refreshToken) {
}
