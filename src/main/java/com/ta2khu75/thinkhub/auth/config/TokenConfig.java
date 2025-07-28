package com.ta2khu75.thinkhub.auth.config;

public record TokenConfig(String secret, long expiration) {
}