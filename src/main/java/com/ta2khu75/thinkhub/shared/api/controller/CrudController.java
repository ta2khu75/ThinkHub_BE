package com.ta2khu75.thinkhub.shared.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public interface CrudController<Request, Response, Id> {
	@PostMapping
	ResponseEntity<Response> create(@Valid @RequestBody Request request);

	@PutMapping("{id}")
	ResponseEntity<Response> update(@PathVariable Id id, @Valid @RequestBody Request request);

	@DeleteMapping("{id}")
	ResponseEntity<Void> delete(@PathVariable Id id);

	@GetMapping("{id}")
	ResponseEntity<Response> read(@PathVariable Id id);
}
