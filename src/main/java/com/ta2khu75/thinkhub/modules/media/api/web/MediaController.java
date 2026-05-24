package com.ta2khu75.thinkhub.modules.media.api.web;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.modules.media.api.MediaApi;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Media", description = "Create, update and delete media")
@ApiController("${app.api-prefix}/medias")
public class MediaController extends BaseController<MediaApi> {

	protected MediaController(MediaApi api) {
		super(api);
	}

	@PostMapping
	public ResponseEntity<MediaResponse> create(@ModelAttribute MediaRequest request) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(api.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		api.delete(id);
		return ResponseEntity.noContent().build();
	}
}
