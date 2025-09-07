package com.ta2khu75.thinkhub.authentication.api.dto;

import com.ta2khu75.thinkhub.authentication.internal.model.ProviderType;

public record SocialLoginRequest(ProviderType provider, String token) {
}
