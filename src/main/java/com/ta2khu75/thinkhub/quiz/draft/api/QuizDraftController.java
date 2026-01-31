package com.ta2khu75.thinkhub.quiz.draft.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftUpdateRequest;
import com.ta2khu75.thinkhub.quiz.draft.internal.service.QuizDraftService;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "QuizDraft", description = "Create, manage and interact with quizzes including commenting and reporting.")
@ApiController("${app.api-prefix}/quiz-drafts")
class QuizDraftController extends BaseController<QuizDraftService> {

	public QuizDraftController(QuizDraftService service) {
		super(service);
	}

	@PostMapping
	@Operation(summary = "Create a new quiz draft", description = "Create a new quiz draft with optional image to test user knowledge.")
	public ResponseEntity<QuizDraftResponse> create(QuizDraftCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@PutMapping
	@Operation(summary = "Update a new quiz draft", description = "Update a new quiz draft with optional image to test user knowledge.")
	public ResponseEntity<Void> update(String id, QuizDraftUpdateRequest request) {
		service.update(id, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping
	@Operation(summary = "Read a new quiz draft", description = "Read a new quiz draft with optional image to test user knowledge.")
	public ResponseEntity<QuizDraftResponse> read(String id) {
		return ResponseEntity.ok(service.read(id));
	}

	@DeleteMapping
	@Operation(summary = "Delete a new quiz draft", description = "Delete a new quiz draft with optional image to test user knowledge.")
	public ResponseEntity<Void> delete(String id, QuizDraftUpdateRequest request) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
