package com.ta2khu75.thinkhub.authorization.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.ta2khu75.thinkhub.authorization.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authorization.internal.group.PermissionGroupService;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Permission Groups", description = "Helps organize related permissions into groups so they can be managed more easily.")
@ApiController("${app.api-prefix}/authority/permission-groups")
public class PermissionGroupController extends BaseController<PermissionGroupService> {

	protected PermissionGroupController(PermissionGroupService service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "View all permission groups", description = "Use this endpoint to see all available permission groups. Each group helps organize permissions based on functionality, making it easier to assign and manage them for different roles.")
	public ResponseEntity<List<PermissionGroupResponse>> readAll() {
		return ResponseEntity.ok(service.readAll());
	}
}
