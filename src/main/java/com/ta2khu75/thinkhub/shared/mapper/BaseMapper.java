package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<REQ, Entity> {
	Entity toEntity(REQ request);

	void update(REQ request, @MappingTarget Entity entity);
}
