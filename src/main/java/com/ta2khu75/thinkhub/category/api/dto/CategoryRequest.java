package com.ta2khu75.thinkhub.category.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String name, String description) {

}
