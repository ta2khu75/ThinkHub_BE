package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.ta2khu75.thinkhub.authority.entity.PermissionGroup;
import com.ta2khu75.thinkhub.authority.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.anotation.MappingConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MappingConfig.class)
public interface PermissionGroupMapper
		extends BaseMapper<PermissionGroupRequest, PermissionGroupResponse, PermissionGroup> {
	@Override
	PermissionGroupResponse toResponse(PermissionGroup entity);

	@Override
	PermissionGroup toEntity(PermissionGroupRequest request);

	@Override
	void update(PermissionGroupRequest request, @MappingTarget PermissionGroup entity);

}
