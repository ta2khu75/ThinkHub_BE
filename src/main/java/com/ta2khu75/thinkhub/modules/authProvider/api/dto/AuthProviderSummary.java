package com.ta2khu75.thinkhub.modules.authProvider.api.dto;

import com.ta2khu75.thinkhub.modules.authProvider.internal.entity.ProviderType;

public record AuthProviderSummary(Long id, ProviderType type, String password, String userId) {

}
