package com.ta2khu75.thinkhub.post.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.post.internal.entity.PostStatus;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse extends BaseClassResponse<String> {
	String title;
	String content;
	String slug;
	PostStatus status;
	Set<TagDto> tags;
	AuthorResponse author;
	String imageUrl;
	Long categoryId;
	int viewCount;
}