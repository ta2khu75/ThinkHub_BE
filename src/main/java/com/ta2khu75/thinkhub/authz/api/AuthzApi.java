package com.ta2khu75.thinkhub.authz.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;

public interface AuthzApi {
	RoleResponse readRole(Long id);

	RoleResponse readRoleByName(String name);

	RoleResponse createRole(RoleRequest request);

	RoleResponse updateRole(Long id, RoleRequest request);

	List<RoleResponse> readAllRoles();

	RoleSummary readRoleSummary(Long id);

	RoleSummary readRoleSummaryByName(String name);

	void deleteRole(Long id);

	List<PermissionGroupResponse> readAllGroups();

	Set<PermissionGroupSummary> readAllGroupSummaryByCodes(Collection<String> codes);

	List<PermissionGroupSummary> saveAllGroups(Collection<PermissionGroupSummary> groups);

	Set<PermissionSummary> readAllPermissionSummaryByCodes(Collection<String> codes);

	List<PermissionSummary> saveAllPermisisons(Collection<PermissionSummary> permissions);

	void assignPermissionsToRole(String roleName, Set<Long> permissionIds);
}
