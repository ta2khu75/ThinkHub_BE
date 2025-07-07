package com.ta2khu75.thinkhub.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.group.Admin;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

public class AccountController extends BaseController<AccountService> {

	protected AccountController(AccountService service) {
		super(service);
	}

	@PostMapping
	@Validated(value = { Default.class, Admin.class })
	public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@DeleteMapping
	@EndpointMapping(name = "Delete account")
	ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("profile/{id}")
	@EndpointMapping(name = "Read profile")
	ResponseEntity<AccountProfileResponse> read(@PathVariable Long id) {
		return ResponseEntity.ok(service.readProfile(id));
	}

	@GetMapping
	@EndpointMapping(name = "Search account")
	public ResponseEntity<PageResponse<AccountResponse>> search(@SnakeCaseModelAttribute AccountSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PutMapping("profile")
	@EndpointMapping(name = "Update profile")
	public ResponseEntity<AccountProfileResponse> updateProfile(@PathVariable Long id,
			@Valid @RequestBody AccountProfileRequest request) {
		return ResponseEntity.ok(service.updateProfile(id, request));
	}

	@PutMapping("status/{id}")
	@EndpointMapping(name = "Update status")
	public ResponseEntity<AccountStatusResponse> updateStatus(@PathVariable Long id,
			@Valid @RequestBody AccountStatusRequest request) {
		return ResponseEntity.ok(service.updateStatus(id, request));
	}

	@PutMapping("password/{id}")
	@EndpointMapping(name = "Update password")
	public ResponseEntity<AccountResponse> updateStatus(@PathVariable String id,
			@Valid @RequestBody AccountPasswordRequest request) {
		return ResponseEntity.ok(service.updatePassword(id, request));
	}
}
