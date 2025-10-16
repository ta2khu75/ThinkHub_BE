package com.ta2khu75.thinkhub.authn.internal.model;

public record ProviderUser(String userId, String email, String firstName, String lastName, String pictureUrl,
		ProviderType provider) {
}
