package com.ta2khu75.thinkhub.authz.internal.permission;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface PermissionMapper extends Converter<Permission, PermissionResponse> {
	@Override
	PermissionResponse convert(Permission entity);

	@Mapping(target = "permissionIds", ignore = true)
	PermissionSummary toSummary(Permission entity);

	@Mapping(target = "permissions", ignore = true)
	Permission toEntity(PermissionSummary summary);
}
