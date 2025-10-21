package com.ta2khu75.thinkhub.media.api.dto;

import com.ta2khu75.thinkhub.media.internal.entity.MediaOwnerType;
import com.ta2khu75.thinkhub.media.internal.entity.MediaType;

public record MediaResponse(String filename, String url, Long size, MediaType type, MediaOwnerType owner,
		String ownerId, String uploadedBy) {

}
