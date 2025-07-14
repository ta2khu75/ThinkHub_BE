package com.ta2khu75.thinkhub.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(Long id,@NotBlank String name, String description) {

}
