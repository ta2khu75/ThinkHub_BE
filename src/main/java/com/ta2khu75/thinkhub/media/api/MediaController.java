package com.ta2khu75.thinkhub.media.api;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.ta2khu75.thinkhub.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Media", description = "Create, update and delete media")
@ApiController("${app.api-prefix}/media")
public class MediaController extends BaseController<MediaApi> {

	protected MediaController(MediaApi service) {
		super(service);
	}

	@PostMapping
	public ResponseEntity<MediaResponse> create(@ModelAttribute MediaRequest request) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MediaResponse> upload(@PathVariable Long id, @ModelAttribute MediaRequest request)
			throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
