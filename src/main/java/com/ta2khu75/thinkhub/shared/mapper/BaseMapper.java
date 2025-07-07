package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<REQ, RES, Entity>{
	Entity toEntity(REQ request);
	RES toResponse(Entity entity);
	void update(REQ request, @MappingTarget Entity entity);
}
