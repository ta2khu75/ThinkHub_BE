package com.ta2khu75.thinkhub.authn.internal.config;

public record TokenConfig(String secret, long expiration) {
}