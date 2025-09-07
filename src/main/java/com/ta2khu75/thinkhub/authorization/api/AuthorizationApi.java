package com.ta2khu75.thinkhub.authorization.api;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.authorization.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;

public interface AuthorizationApi {
	List<PermissionGroupResponse> readAllGroups();

	List<RoleResponse> readAllRoles();

	RoleResponse readRole(Long id);

	RoleResponse readRoleByName(String name);

	RoleResponse createRole(RoleRequest request);

	RoleResponse updateRole(Long id, RoleRequest request);

	void deleteRole(Long id);

	Long initDefaultRoles();

	Set<Long> initPermissionsFromEndpoints();

	void assignPermissionsToRole(String roleName, Set<Long> permissionIds);
}
