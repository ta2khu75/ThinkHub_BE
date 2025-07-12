package com.ta2khu75.thinkhub.tag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.tag.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@ApiController("${app.api-prefix}/tags")
@Tag(name = "Tag Management", description = "APIs for managing tags")
public class TagController extends BaseController<TagService> {
	protected TagController(TagService service) {
		super(service);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete a tag", description = "Permanently deletes a tag by its ID")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
