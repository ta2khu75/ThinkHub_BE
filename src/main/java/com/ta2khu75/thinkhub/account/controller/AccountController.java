package com.ta2khu75.thinkhub.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountSearch;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Account", description = "Manage user accounts, profiles and statuses")
@ApiController("${app.api-prefix}/accounts")
public class AccountController extends BaseController<AccountService> {

	protected AccountController(AccountService service) {
		super(service);
	}

	@PostMapping
	@Operation(summary = "Create a new account", description = "Create a new user account with profile and status information.")
	public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete an account", description = "Delete an existing user account by its ID.")
	ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{accountId}/profile")
	@Operation(summary = "Get account profile", description = "Retrieve the profile details of a specific user account.")
	ResponseEntity<AccountProfileResponse> read(@PathVariable Long accountId) {
		return ResponseEntity.ok(service.readProfile(accountId));
	}

	@GetMapping
	@Operation(summary = "Search accounts", description = "Search for user accounts with filtering, sorting, and pagination.")
	public ResponseEntity<PageResponse<AccountResponse>> search(@SnakeCaseModelAttribute AccountSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PutMapping("{accountId}/profile")
	@Operation(summary = "Update account profile", description = "Update the profile information of a specific user account.")
	public ResponseEntity<AccountProfileResponse> updateProfile(@PathVariable Long accountId,
			@Valid @RequestBody AccountProfileRequest request) {
		return ResponseEntity.ok(service.updateProfile(accountId, request));
	}

	@PutMapping("{accountId}/status")
	@Operation(summary = "Update account status", description = "Enable, disable, or lock/unlock a specific user account.")
	public ResponseEntity<AccountStatusResponse> updateStatus(@PathVariable Long accountId,
			@Valid @RequestBody AccountStatusRequest request) {
		return ResponseEntity.ok(service.updateStatus(accountId, request));
	}
}
