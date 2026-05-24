package com.ta2khu75.thinkhub.modules.category.api.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.modules.category.api.CategoryApi;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.controller.CrudController;
import com.ta2khu75.thinkhub.shared.validation.group.Create;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

@Tag(name = "Category", description = "Manage content categories for organizing and classifying items within the system.")
@ApiController("${app.api-prefix}/categories")
public class CategoryController extends BaseController<CategoryApi>
		implements CrudController<CategoryRequest, CategoryResponse, Long> {

	protected CategoryController(CategoryApi api) {
		super(api);
	}

	@Override
	@Validated({ Default.class, Create.class })
	public ResponseEntity<CategoryResponse> create(@Valid CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(api.create(request));
	}

	@Override
	public ResponseEntity<CategoryResponse> update(Long id, @Valid CategoryRequest request) {
		return ResponseEntity.ok(api.update(id, request));
	}

	@Override
	public ResponseEntity<Void> delete(Long id) {
		api.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<CategoryResponse> read(Long id) {
		return ResponseEntity.ok(api.read(id));
	}

	@PostMapping("{id}/activate")
	@Operation(summary = "Activate category", description = "Transition category status from INACTIVE to ACTIVE.")
	public ResponseEntity<CategoryResponse> activate(@PathVariable Long id) {
		return ResponseEntity.ok(api.activate(id));
	}

	@PostMapping("{id}/deactivate")
	@Operation(summary = "Deactivate category", description = "Transition category status from ACTIVE to INACTIVE.")
	public ResponseEntity<CategoryResponse> deactivate(@PathVariable Long id) {
		return ResponseEntity.ok(api.deactivate(id));
	}

	@GetMapping
	@Operation(summary = "Get all categories", description = "Returns a full list of categories without pagination. Useful for dropdowns, filters, or initialization data.")
	public ResponseEntity<List<CategoryResponse>> readAll() {
		return ResponseEntity.ok(api.readAll());
	}
}
