package com.ta2khu75.thinkhub.authentication.internal.config;

public record TokenConfig(String secret, long expiration) {
}