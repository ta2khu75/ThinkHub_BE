package com.ta2khu75.thinkhub.modules.tag.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TagDto(Long id,@NotBlank String name) {

}
