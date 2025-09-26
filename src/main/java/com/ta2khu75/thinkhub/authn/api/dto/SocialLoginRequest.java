package com.ta2khu75.thinkhub.authn.api.dto;

import com.ta2khu75.thinkhub.authn.internal.model.ProviderType;

public record SocialLoginRequest(ProviderType provider, String token) {
}
