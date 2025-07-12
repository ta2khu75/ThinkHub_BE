package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.authority.entity.PermissionGroup;
import com.ta2khu75.thinkhub.authority.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MapperSpringConfig.class)
public interface PermissionGroupMapper extends Converter<PermissionGroup, PermissionGroupResponse>,
		BaseMapper<PermissionGroupRequest, PermissionGroupResponse, PermissionGroup> {
	@Override
	PermissionGroupResponse convert(PermissionGroup entity);

	@Override
	PermissionGroupResponse toResponse(PermissionGroup entity);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	PermissionGroup toEntity(PermissionGroupRequest request);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	void update(PermissionGroupRequest request, @MappingTarget PermissionGroup entity);

}
