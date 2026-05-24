package com.ta2khu75.thinkhub.modules.comment.api.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.modules.comment.api.CommentApi;
import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.modules.comment.api.model.CommentTargetType;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "Manage user comments and allow reporting of inappropriate content.")
@ApiController("${app.api-prefix}/quizzes")
public class QuizCommentController extends BaseController<CommentApi> {

	protected QuizCommentController(CommentApi api) {
		super(api);
	}

	@PostMapping("{quizId}/comments")
	@Operation(summary = "Add a comment to a quiz", description = "Leave a comment or feedback on a specific quiz.")
	public ResponseEntity<CommentResponse> comment(@PathVariable String quizId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(api.create(quizId, CommentTargetType.QUIZ, request));
	}

	@GetMapping("{quizId}/comments")
	@Operation(summary = "View comments on a quiz", description = "Retrieve all user comments on a specific quiz, with support for pagination.")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComments(@PathVariable String quizId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(api.readPageBy(quizId, CommentTargetType.QUIZ, search));
	}

}
