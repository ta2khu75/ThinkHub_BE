package com.ta2khu75.thinkhub.modules.media.api.dto;

import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaStatus;
import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaType;

public record MediaResponse(Long id, String filename, String url, Long size, MediaType type, MediaStatus status,
		Long createdBy) {

}
