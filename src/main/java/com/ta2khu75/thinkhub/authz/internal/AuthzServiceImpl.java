package com.ta2khu75.thinkhub.authz.internal;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authz.internal.group.PermissionGroupService;
import com.ta2khu75.thinkhub.authz.internal.permission.PermissionService;
import com.ta2khu75.thinkhub.authz.internal.role.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthzServiceImpl implements AuthzApi {
	PermissionGroupService groupService;
	PermissionService permissionService;
	RoleService roleService;

	@Override
	public List<PermissionGroupResponse> readAllGroups() {
		return groupService.readAll();
	}

	@Override
	public List<RoleResponse> readAllRoles() {
		return roleService.readAll();
	}

	@Override
	public RoleResponse readRole(Long id) {
		return roleService.read(id);
	}

	@Override
	public RoleResponse readRoleByName(String name) {
		return roleService.readByName(name);
	}

	@Override
	public RoleResponse createRole(RoleRequest request) {
		return roleService.create(request);
	}

	@Override
	public RoleResponse updateRole(Long id, RoleRequest request) {
		return roleService.update(id, request);
	}

	@Override
	public void deleteRole(Long id) {
		roleService.delete(id);
	}

	@Override
	public RoleSummary readRoleSummary(Long id) {
		return roleService.readSummary(id);
	}

	@Override
	public RoleSummary readRoleSummaryByName(String name) {
		return roleService.readSummaryByName(name);
	}

	@Override
	public void assignPermissionsToRole(String roleName, Set<Long> permissionIds) {
		RoleResponse role = roleService.readByName(roleName);
		if (role.permissionIds() == null) {
			roleService.update(role.id(), new RoleRequest(role.name(), role.description(), permissionIds));
		} else {
			roleService.update(role.id(), new RoleRequest(role.name(), role.description(),
					Stream.concat(role.permissionIds().stream(), permissionIds.stream()).collect(Collectors.toSet())));
		}

	}

	@Override
	public List<PermissionGroupSummary> saveAllGroups(Collection<PermissionGroupSummary> groups) {
		return groupService.saveAll(groups);
	}

	@Override
	public List<PermissionSummary> saveAllPermisisons(Collection<PermissionSummary> permissions) {
		return permissionService.saveAll(permissions);
	}

	@Override
	public Set<PermissionGroupSummary> readAllGroupSummaryByCodes(Collection<String> codes) {
		return groupService.readAllSummaryByCodes(codes);
	}

	@Override
	public Set<PermissionSummary> readAllPermissionSummaryByCodes(Collection<String> codes) {
		return permissionService.readAllSummaryByCodes(codes);
	}

}
