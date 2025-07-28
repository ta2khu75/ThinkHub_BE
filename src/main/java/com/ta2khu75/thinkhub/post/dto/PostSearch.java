package com.ta2khu75.thinkhub.post.dto;

import java.time.LocalDate;
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
	private Integer minViews;
	private Integer maxViews;
	private String authorId;
	private Long authorIdQuery;
	private AccessModifier accessModifier;
	private LocalDate createdFrom;
	private LocalDate createdTo;
}