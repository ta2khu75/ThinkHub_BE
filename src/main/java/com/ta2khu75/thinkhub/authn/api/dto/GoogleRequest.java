package com.ta2khu75.thinkhub.authn.api.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleRequest(@NotBlank String accessToken) {

}
