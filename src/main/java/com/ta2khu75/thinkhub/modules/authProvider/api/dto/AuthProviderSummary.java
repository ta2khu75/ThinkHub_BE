package com.ta2khu75.thinkhub.modules.authProvider.api.dto;

import com.ta2khu75.thinkhub.modules.authProvider.api.model.ProviderType;

public record AuthProviderSummary(Long id, ProviderType type, String password, Long userId) {

}
