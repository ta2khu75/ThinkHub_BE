package com.ta2khu75.thinkhub.authn.internal.model;

public record GoogleUser(String userId, String email, boolean emailVerified, String name, String pictureUrl) {
}
