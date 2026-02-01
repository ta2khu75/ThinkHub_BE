package com.ta2khu75.thinkhub.modules.media.api.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.modules.media.internal.anotation.FileNotEmpty;
import com.ta2khu75.thinkhub.modules.media.internal.entity.MediaOwnerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MediaRequest(@FileNotEmpty MultipartFile file, @NotNull MediaOwnerType owner, @NotBlank String ownerId) {
}
