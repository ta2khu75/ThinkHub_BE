package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ta2khu75.thinkhub.authority.RoleDto;
import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.entity.Role;
import com.ta2khu75.thinkhub.authority.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface RoleMapper extends Converter<Role, RoleResponse>, BaseMapper<RoleRequest, RoleResponse, Role> {
	@Override
	Role toEntity(RoleRequest request);

	RoleDto toDto(Role role);

	@Override
	void update(RoleRequest request, @MappingTarget Role role);

	@Override
	@Mapping(target = "permissionIds", source = "permissions")
	RoleResponse toResponse(Role role);

	default Long map(Permission permission) {
		return permission == null ? null : permission.getId();
	}
}
