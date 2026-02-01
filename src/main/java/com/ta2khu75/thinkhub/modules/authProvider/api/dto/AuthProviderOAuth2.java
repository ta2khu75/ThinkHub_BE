package com.ta2khu75.thinkhub.modules.authProvider.api.dto;

import com.ta2khu75.thinkhub.modules.authProvider.internal.entity.ProviderType;

public record AuthProviderOAuth2(ProviderType type, String providerId, String email, String userId) {

}
