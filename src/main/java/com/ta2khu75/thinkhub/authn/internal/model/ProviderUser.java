package com.ta2khu75.thinkhub.authn.internal.model;

import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;

public record ProviderUser(String userId, String email, String firstName, String lastName, String pictureUrl,
		ProviderType provider) {
}
