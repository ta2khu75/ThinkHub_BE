package com.ta2khu75.thinkhub.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@ApiController("${app.api-prefix}/users")
public class UserController extends BaseController<UserApi> implements IdDecodable {

	protected UserController(UserApi service) {
		super(service);
	}

//	@PostMapping
//	@Operation(summary = "Create a new account", description = "Create a new user account with profile and status information.")
//	public ResponseEntity<UserResponse> create(@Valid @RequestBody request) {
//		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
//	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete an account", description = "Delete an existing user account by its ID.")
	ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(this.decodeUserId(id));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	ResponseEntity<UserResponse> read(@PathVariable String id) {
		return ResponseEntity.ok(service.read(this.decodeUserId(id)));
	}

	@GetMapping
	@Operation(summary = "Search accounts", description = "Search for user accounts with filtering, sorting, and pagination.")
	public ResponseEntity<PageResponse<UserResponse>> search(@SnakeCaseModelAttribute UserSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PutMapping("{accountId}/profile")
	@Operation(summary = "Update account profile", description = "Update the profile information of a specific user account.")
	public ResponseEntity<UserResponse> updateProfile(@PathVariable String accountId,
			@Valid @RequestBody UserRequest request) {
		return ResponseEntity.ok(service.update(this.decodeId(accountId), request));
	}

	@PutMapping("{accountId}/status")
	@Operation(summary = "Update account status", description = "Enable, disable, or lock/unlock a specific user account.")
	public ResponseEntity<UserStatusResponse> updateStatus(@PathVariable String accountId,
			@Valid @RequestBody UserStatusRequest request) {
		return ResponseEntity.ok(service.updateStatus(this.decodeId(accountId), request));
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}
}
