package com.ta2khu75.thinkhub.authority.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.authority.api.dto.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.authority.internal.entity.Permission;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MapperSpringConfig.class)
public interface PermissionMapper
		extends Converter<Permission, PermissionResponse>, BaseMapper<PermissionRequest, Permission> {
	@Override
	PermissionResponse convert(Permission entity);

	@Override
	@Mapping(target = "id", ignore = true)
	Permission toEntity(PermissionRequest request);

	@Override
	@Mapping(target = "id", ignore = true)
	void update(PermissionRequest request, @MappingTarget Permission entity);

}
