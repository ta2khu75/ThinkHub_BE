package com.ta2khu75.thinkhub.modules.user.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Me")
@ApiController("${app.api-prefix}/me")
public class MeController extends BaseController<UserApi> {

	protected MeController(UserApi api) {
		super(api);
	}

	@GetMapping
	public ResponseEntity<UserResponse> readMe() {
		return ResponseEntity.ok(api.readMe());
	}

	@PutMapping
	public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.ok(api.updateMe(request));
	}
}
