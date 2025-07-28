package com.ta2khu75.thinkhub.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.ta2khu75.thinkhub.category.CategoryService;
import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.controller.CrudController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Category", description = "Manage content categories for organizing and classifying items within the system.")
@ApiController("${app.api-prefix}/categories")
public class CategoryController extends BaseController<CategoryService>
		implements CrudController<CategoryRequest, CategoryResponse, Long> {

	protected CategoryController(CategoryService service) {
		super(service);
	}

	@Override
	@Operation(summary = "Create a new category", description = "Add a new category to help organize or classify content.")
	public ResponseEntity<CategoryResponse> create(@Valid CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@Override
	@Operation(summary = "Update an existing category", description = "Change the name or properties of a specific category.")
	public ResponseEntity<CategoryResponse> update(Long id, @Valid CategoryRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@Override
	@Operation(summary = "Delete a category", description = "Remove a category permanently from the system.")
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Get a category", description = "Retrieve details about a specific category.")
	public ResponseEntity<CategoryResponse> read(Long id) {
		return ResponseEntity.ok(service.read(id));
	}

	@GetMapping
	@Operation(summary = "Get all categories", description = "Returns a full list of categories without pagination. Useful for dropdowns, filters, or initialization data.")
	public ResponseEntity<List<CategoryResponse>> readAll() {
		return ResponseEntity.ok(service.readAll());
	}
}
