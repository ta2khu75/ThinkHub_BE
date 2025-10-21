package com.ta2khu75.thinkhub.post.api.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
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
	Set<TagDto> tags;
	AuthorResponse author;
	String imageUrl;
	Long categoryId;
	int viewCount;
	AccessModifier accessModifier;
}