package com.ta2khu75.thinkhub.modules.post.draft.api.dto;

import java.util.List;

public record PostDraftUpdateRequest(String title, String content, Long mediaId, List<String> tagNames,
		Long categoryId) {
}
