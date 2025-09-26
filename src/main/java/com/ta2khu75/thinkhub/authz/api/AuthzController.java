package com.ta2khu75.thinkhub.authz.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authz", description = "APIs for managing roles and permission groups. "
		+ "Use these endpoints to create, update, delete, or view roles and their permissions.")
@ApiController("${app.api-prefix}/authz")
public class AuthzController extends BaseController<AuthzApi> {
	protected AuthzController(AuthzApi service) {
		super(service);
	}

	@GetMapping("roles")
	@Operation(summary = "View all roles", description = "Use this endpoint to see a list of all roles that exist in the system. Roles define what permissions or access levels a user can have.")
	public ResponseEntity<List<RoleResponse>> readAllRoles() {
		return ResponseEntity.ok(service.readAllRoles());
	}

	@PostMapping("roles")
	@Operation(summary = "Add a new role", description = "Use this to create a new role. A role usually groups a set of permissions that can later be assigned to users.")
	public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(request));
	}

	@PutMapping("roles/{id}")
	@Operation(summary = "Edit an existing role", description = "Update the name or permissions of a specific role. Useful when access needs change over time.")
	public ResponseEntity<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
		return ResponseEntity.ok(service.updateRole(id, request));
	}

	@DeleteMapping("roles/{id}")
	@Operation(summary = "Remove a role", description = "Deletes a role from the system. Be careful—users assigned to this role will lose associated permissions.")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.deleteRole(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("roles/{id}")
	@Operation(summary = "Get role details", description = "View details of a specific role by its ID, including what permissions it has.")
	public ResponseEntity<RoleResponse> read(@PathVariable Long id) {
		return ResponseEntity.ok(service.readRole(id));
	}

	@GetMapping("permission-groups")
	@Operation(summary = "View all permission groups", description = "Use this endpoint to see all available permission groups. Each group helps organize permissions based on functionality, making it easier to assign and manage them for different roles.")
	public ResponseEntity<List<PermissionGroupResponse>> readAllGroups() {
		return ResponseEntity.ok(service.readAllGroups());
	}
}
