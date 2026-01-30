package com.ta2khu75.thinkhub.post.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.ta2khu75.thinkhub.post.internal.entity.PostStatus;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostSearch extends Search {
	private List<Long> tagIds;
	private List<Long> categoryIds;
	private Integer minViews;
	private Integer maxViews;
	private String ownerId;
	private Long ownerIdQuery;
	private PostStatus status;
	private LocalDate createdFrom;
	private LocalDate createdTo;
}