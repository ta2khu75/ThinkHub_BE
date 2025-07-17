package com.ta2khu75.thinkhub.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "Manage user comments and allow reporting of inappropriate content.")
@ApiController("${app.api-prefix}/comments")
public class CommentController extends BaseController<CommentService> {

	protected CommentController(CommentService service) {
		super(service);
	}

	@DeleteMapping
	@Operation(summary = "Delete a comment", description = "Remove a specific comment from a post or discussion thread.")
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	@Operation(summary = "Update a comment", description = "Edit the content of an existing comment.")
	public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody CommentRequest content) {
		return ResponseEntity.ok(service.update(id, content));
	}

	@PostMapping("{id}/reports")
	@Operation(summary = "Report a comment", description = "Flag a comment as inappropriate or offensive for moderation.")
	public ResponseEntity<ReportResponse> report(@PathVariable Long id, @RequestBody ReportRequest content) {
		return ResponseEntity.ok(service.report(id, content));
	}
}
