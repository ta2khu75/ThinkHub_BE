package com.ta2khu75.thinkhub.auth.internal.config;

public record TokenConfig(String secret, long expiration) {
}