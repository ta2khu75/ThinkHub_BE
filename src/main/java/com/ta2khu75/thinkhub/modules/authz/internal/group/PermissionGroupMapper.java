package com.ta2khu75.thinkhub.modules.authz.internal.group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface PermissionGroupMapper extends Converter<PermissionGroup, PermissionGroupResponse> {
	@Override
	PermissionGroupResponse convert(PermissionGroup entity);

	@Mapping(target = "permissionIds", ignore = true)
	PermissionGroupSummary toSummary(PermissionGroup permissionGroup);

	@Mapping(target = "permissions", ignore = true)
	PermissionGroup toEntity(PermissionGroupSummary permissionGroupSummary);

}
