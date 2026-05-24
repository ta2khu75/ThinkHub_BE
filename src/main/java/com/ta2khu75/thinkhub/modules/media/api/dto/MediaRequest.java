package com.ta2khu75.thinkhub.modules.media.api.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.modules.media.internal.anotation.FileNotEmpty;

public record MediaRequest(@FileNotEmpty MultipartFile file) {
}
