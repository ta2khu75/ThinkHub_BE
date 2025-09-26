package com.ta2khu75.thinkhub.authz.api;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;

public interface AuthzApi {
	List<PermissionGroupResponse> readAllGroups();

	List<RoleResponse> readAllRoles();

	RoleResponse readRole(Long id);

	RoleDto readRoleDto(Long id);

	RoleDto readRoleDtoByName(String name);

	RoleResponse readRoleByName(String name);

	RoleResponse createRole(RoleRequest request);

	RoleResponse updateRole(Long id, RoleRequest request);

	void deleteRole(Long id);

	Long initDefaultRoles();

	Set<Long> initPermissionsFromEndpoints();

	void assignPermissionsToRole(String roleName, Set<Long> permissionIds);
}
