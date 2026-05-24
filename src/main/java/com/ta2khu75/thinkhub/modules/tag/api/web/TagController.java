package com.ta2khu75.thinkhub.modules.tag.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ta2khu75.thinkhub.modules.tag.api.TagApi;
import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@ApiController("${app.api-prefix}/tags")
@Tag(name = "Tag", description = "APIs for managing tags")
public class TagController extends BaseController<TagApi> {
	protected TagController(TagApi api) {
		super(api);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete a tag", description = "Permanently deletes a tag by its ID")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		api.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<PageResponse<TagDto>> search(@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(api.search(search));
	}
}
