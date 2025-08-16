package com.ta2khu75.thinkhub.tag.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TagResponse(Long id, @NotBlank String name) {

}
