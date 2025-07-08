package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MapperSpringConfig.class)
public interface PermissionMapper extends Converter<Permission, PermissionResponse>,
		BaseMapper<PermissionRequest, PermissionResponse, Permission> {
	@Override
	PermissionResponse toResponse(Permission entity);

	@Override
	Permission toEntity(PermissionRequest request);

	@Override
	void update(PermissionRequest request, @MappingTarget Permission entity);
	
}
