package com.ta2khu75.thinkhub.shared.mapper;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.shared.dto.PageResponse;


public interface PageMapper<E,RES> {
	default PageResponse<RES> toPageResponse(Page<E> page) {
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), page.map(this::toResponse).getContent());
	}

	default PageResponse<E> toRawPageResponse(Page<E> page) {
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), page.getContent());
	}
	
	RES toResponse(E source);
}
