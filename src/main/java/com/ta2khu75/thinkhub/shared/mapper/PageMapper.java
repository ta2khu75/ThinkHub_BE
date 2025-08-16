package com.ta2khu75.thinkhub.shared.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;

public interface PageMapper<E, RES> extends Converter<E, RES> {

	default PageResponse<RES> toPageResponse(Page<E> page) {
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(),
				page.map(this::convert).getContent());
	}

	@Override
	RES convert(E source);
}
