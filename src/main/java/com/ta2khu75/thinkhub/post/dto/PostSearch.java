package com.ta2khu75.thinkhub.post.dto;

import java.util.List;

import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostSearch extends Search {
	private List<Long> tagIds;
	private List<Long> categoryIds;
	private Integer minView;
	private Integer maxView;
	private String authorId;
	private Long authorIdQuery;
	private AccessModifier accessModifier;
}