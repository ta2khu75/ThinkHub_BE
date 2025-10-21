package com.ta2khu75.thinkhub.category.api.dto;

import com.ta2khu75.thinkhub.shared.group.Create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(@NotBlank String name, String description,
		@NotNull(groups = Create.class) Long mediaId, @NotNull(groups = Create.class) Long defaultMediaId) {
}
