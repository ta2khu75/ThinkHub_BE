package com.ta2khu75.thinkhub.modules.authn.internal.model;

import com.ta2khu75.thinkhub.modules.authProvider.api.model.ProviderType;

public record ProviderUser(String userId, String email, String firstName, String lastName, String pictureUrl,
		ProviderType provider) {
}
