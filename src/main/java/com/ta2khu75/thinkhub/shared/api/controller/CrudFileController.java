package com.ta2khu75.thinkhub.shared.api.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

public interface CrudFileController<Request, Response, Id> {
	@PostMapping
	ResponseEntity<Response> create(@Valid @RequestPart Request request, @RequestPart(required = false) MultipartFile file) throws IOException;

	@PutMapping("{id}")
	ResponseEntity<Response> update(@PathVariable Id id, @Valid @RequestPart Request request, @RequestPart(required = false) MultipartFile file) throws IOException;

	@DeleteMapping("{id}")
	ResponseEntity<Void> delete(@PathVariable Id id);

	@GetMapping("{id}")
	ResponseEntity<Response> read(@PathVariable Id id);
}
