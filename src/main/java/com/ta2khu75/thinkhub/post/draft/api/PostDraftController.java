package com.ta2khu75.thinkhub.post.draft.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftCreateRequest;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftResponse;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftUpdateRequest;
import com.ta2khu75.thinkhub.post.draft.internal.service.PostDraftService;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post Draft", description = "Post Draft API")
@ApiController("${app.api-prefix}/post-drafts")
class PostDraftController extends BaseController<PostDraftService> {

	public PostDraftController(PostDraftService service) {
		super(service);
	}

	@PostMapping
	@Operation(summary = "Create a new post draft", description = "Create a new post draft with optional image to test user knowledge.")
	public ResponseEntity<PostDraftResponse> create(PostDraftCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@PutMapping
	@Operation(summary = "Update a new post draft", description = "Update a new post draft with optional image to test user knowledge.")
	public ResponseEntity<Void> update(String id, PostDraftUpdateRequest request) {
		service.update(id, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping
	@Operation(summary = "Read a new post draft", description = "Read a new post draft with optional image to test user knowledge.")
	public ResponseEntity<PostDraftResponse> read(String id) {
		return ResponseEntity.ok(service.read(id));
	}

	@DeleteMapping
	@Operation(summary = "Delete a new post draft", description = "Delete a new post draft with optional image to test user knowledge.")
	public ResponseEntity<Void> delete(String id, PostDraftUpdateRequest request) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
