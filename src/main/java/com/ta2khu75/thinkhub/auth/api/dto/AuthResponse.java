package com.ta2khu75.thinkhub.auth.api.dto;

import com.ta2khu75.thinkhub.account.api.dto.response.AccountProfileResponse;

public record AuthResponse(String id, AccountProfileResponse profile, String role, TokenResponse accessToken,
		TokenResponse refreshToken) {
}
