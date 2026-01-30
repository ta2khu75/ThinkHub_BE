package com.ta2khu75.thinkhub.postDraft.api.dto;

import java.time.Instant;
import java.util.List;

public record PostDraftResponse(String id,String title, String content, Long mediaId, String imageUrl, List<String> tagNames,
		Long categoryId, Instant lastModifiedAt) {

}
