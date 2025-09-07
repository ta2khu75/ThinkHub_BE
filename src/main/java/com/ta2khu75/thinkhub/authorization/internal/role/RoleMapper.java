package com.ta2khu75.thinkhub.authorization.internal.role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ta2khu75.thinkhub.authorization.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authorization.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authorization.internal.permission.Permission;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface RoleMapper extends Converter<Role, RoleResponse>, BaseMapper<RoleRequest, Role> {
	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	Role toEntity(RoleRequest request);

	RoleDto toDto(Role role);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	void update(RoleRequest request, @MappingTarget Role role);

	@Override
	@Mapping(target = "permissionIds", source = "permissions")
	RoleResponse convert(Role role);

	default Long map(Permission permission) {
		return permission == null ? null : permission.getId();
	}
}
