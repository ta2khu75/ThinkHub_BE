package com.ta2khu75.thinkhub.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta2khu75.quiz.anotation.EndpointMapping;
import com.ta2khu75.quiz.model.request.CommentRequest;
import com.ta2khu75.quiz.model.response.CommentResponse;
import com.ta2khu75.quiz.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${app.api-prefix}/comments")
public class CommentController extends BaseController<CommentService> {
	public CommentController(CommentService service) {
		super(service);
	}
	@PreAuthorize("@ownerSecurity.isCommentOwner(#id)")
	@EndpointMapping(name = "Update comment")
	public ResponseEntity<CommentResponse> update(@PathVariable String id, @Valid @RequestBody CommentRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@PreAuthorize("@ownerSecurity.isCommentOwner(#id) or hasRole('ROOT')")
	@EndpointMapping(name = "Delete comment")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
