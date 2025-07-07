package com.ta2khu75.thinkhub.authority.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.anotation.MappingConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;

@Mapper(config = MappingConfig.class)
public interface PermissionMapper extends BaseMapper<PermissionRequest, PermissionResponse, Permission> {
	@Override
	@Named("toPermissionResponse")
	PermissionResponse toResponse(Permission entity);

	@Override
	Permission toEntity(PermissionRequest request);

	@Override
	void update(PermissionRequest request, @MappingTarget Permission entity);

}
