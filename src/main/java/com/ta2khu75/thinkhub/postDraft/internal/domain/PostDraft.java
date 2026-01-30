package com.ta2khu75.thinkhub.postDraft.internal.domain;

import java.time.Instant;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDraft {
	String id;
	String title;
	String content;
	Long mediaId;
	List<String> tagNames;
	Long categoryId;
	Long ownerId;
	Instant lastModifiedAt;
}
