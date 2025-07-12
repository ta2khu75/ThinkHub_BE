package com.ta2khu75.thinkhub.tag.dto;

import jakarta.validation.constraints.NotBlank;

public record TagResponse(Long id, @NotBlank String name) {

}
