package com.ta2khu75.thinkhub.authorization.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ta2khu75.thinkhub.authorization.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authorization.internal.role.RoleService;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.controller.CrudController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Roles", description = "Manage roles to control what users can access and do in the system.")
@ApiController("${app.api-prefix}/authority/roles")
public class RoleController extends BaseController<RoleService>
		implements CrudController<RoleRequest, RoleResponse, Long> {
	protected RoleController(RoleService service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "View all roles", description = "Use this endpoint to see a list of all roles that exist in the system. Roles define what permissions or access levels a user can have.")
	public ResponseEntity<List<RoleResponse>> readAll() {
		return ResponseEntity.ok(service.readAll());
	}

	@Override
	@Operation(summary = "Add a new role", description = "Use this to create a new role. A role usually groups a set of permissions that can later be assigned to users.")
	public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@Override
	@Operation(summary = "Edit an existing role", description = "Update the name or permissions of a specific role. Useful when access needs change over time.")
	public ResponseEntity<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@Override
	@Operation(summary = "Remove a role", description = "Deletes a role from the system. Be careful—users assigned to this role will lose associated permissions.")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Get role details", description = "View details of a specific role by its ID, including what permissions it has.")
	public ResponseEntity<RoleResponse> read(@PathVariable Long id) {
		return ResponseEntity.ok(service.read(id));
	}
}
