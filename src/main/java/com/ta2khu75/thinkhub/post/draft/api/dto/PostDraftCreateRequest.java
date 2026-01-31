package com.ta2khu75.thinkhub.post.draft.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

public record PostDraftCreateRequest(String title, Long categoryId) {
}
