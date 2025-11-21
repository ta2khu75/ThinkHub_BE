package com.ta2khu75.thinkhub.authProvider.api.dto;

import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;

public record AuthProviderOAuth2(ProviderType type, String providerId, String email, String userId) {

}
