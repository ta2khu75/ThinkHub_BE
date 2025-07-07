package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.MappingTarget;

import com.ta2khu75.thinkhub.authority.RoleDto;
import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.entity.Role;
import com.ta2khu75.thinkhub.authority.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.anotation.MappingConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MappingConfig.class)
public interface RoleMapper extends BaseMapper<RoleRequest, RoleResponse, Role> {
	Role toEntity(RoleRequest request);

	RoleDto toDto(Role role);

	RoleResponse toResponse(Role role);

	void update(RoleRequest request, @MappingTarget Role role);

	default Long map(Permission permission) {
		return permission == null ? null : permission.getId();
	}
}
