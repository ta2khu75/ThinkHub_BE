package com.ta2khu75.thinkhub.modules.authn.internal.config;

public record TokenConfig(String secret, long expiration) {
}