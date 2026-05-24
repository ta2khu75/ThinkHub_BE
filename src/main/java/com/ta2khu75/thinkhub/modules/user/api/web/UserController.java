package com.ta2khu75.thinkhub.modules.user.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User")
@ApiController("${app.api-prefix}/users")
class UserController extends BaseController<UserApi> {

	protected UserController(UserApi api) {
		super(api);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete an account", description = "Delete an existing user account by its ID.")
	ResponseEntity<Void> delete(@PathVariable String id) {
		api.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	ResponseEntity<UserResponse> read(@PathVariable String id) {
		return ResponseEntity.ok(api.read(id));
	}

	@GetMapping
	@Operation(summary = "Search accounts", description = "Search for user accounts with filtering, sorting, and pagination.")
	public ResponseEntity<PageResponse<UserResponse>> search(@SnakeCaseModelAttribute UserSearch search) {
		return ResponseEntity.ok(api.search(search));
	}

	@PutMapping("{id}")
	@Operation(summary = "Update account profile", description = "Update the profile information of a specific user account.")
	public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
		return ResponseEntity.ok(api.update(id, request));
	}

	@PutMapping("{id}/status")
	@Operation(summary = "Update account status", description = "Enable, disable, or lock/unlock a specific user account.")
	public ResponseEntity<UserStatusResponse> updateStatus(@PathVariable String id,
			@Valid @RequestBody UserStatusRequest request) {
		return ResponseEntity.ok(api.updateStatus(id, request));
	}
}
