package com.ta2khu75.thinkhub.postDraft.api.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

public record PostDraftUpdateRequest(String title, String content, Long mediaId, List<String> tagNames,
		Long categoryId) {
}
