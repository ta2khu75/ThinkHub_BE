package com.ta2khu75.thinkhub.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ta2khu75.thinkhub.category.CategoryService;
import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.controller.CrudController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Category", description = "Endpoints for managing categories")
@ApiController("${app.api-prefix}/category")
public class CategoryController extends BaseController<CategoryService>
		implements CrudController<CategoryRequest, CategoryResponse, Long> {

	protected CategoryController(CategoryService service) {
		super(service);
	}

	@Override
	@Operation(summary = "Create a new category", description = "Adds a new category to the system")
	public ResponseEntity<CategoryResponse> create(@Valid CategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@Override
	@Operation(summary = "Update a category", description = "Updates the details of an existing category by ID")
	public ResponseEntity<CategoryResponse> update(Long id, @Valid CategoryRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@Override
	@Operation(summary = "Delete a category", description = "Deletes a category by its ID")
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Get category details", description = "Fetches category information by ID")
	public ResponseEntity<CategoryResponse> read(Long id) {
		return ResponseEntity.ok(service.read(id));
	}
}
