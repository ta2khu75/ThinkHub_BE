package com.ta2khu75.thinkhub.category.api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
	Long id;
	String name;
	String slug;
	String description;
	String imageUrl;
	String defaultImageUrl;
}
