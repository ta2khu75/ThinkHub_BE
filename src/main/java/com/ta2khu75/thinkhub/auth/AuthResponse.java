package com.ta2khu75.thinkhub.auth;

import com.ta2khu75.thinkhub.account.dto.response.AccountProfileResponse;

public record AuthResponse(String id, AccountProfileResponse profile, String role, String accessToken,
		TokenResponse refreshToken) {
}
