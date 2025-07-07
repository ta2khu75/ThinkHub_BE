package com.ta2khu75.thinkhub.auth;

import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;

public record AuthResponse(AccountProfileResponse profile, String role, String accessToken, TokenResponse refreshToken) {
}
