package com.ta2khu75.thinkhub.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Comment", description = "Manage user comments and allow reporting of inappropriate content.")
@ApiController("${app.api-prefix}/quizzes")
public class QuizCommentController extends BaseController<CommentService> implements IdDecodable{
	
	protected QuizCommentController(CommentService service) {
		super(service);
	}

	@PostMapping("{quizId}/comments")
	@Operation(summary = "Add a comment to a quiz", description = "Leave a comment or feedback on a specific quiz.")
	public ResponseEntity<CommentResponse> comment(@PathVariable String quizId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(decodeId(quizId), CommentTargetType.QUIZ, request));
	}

	@GetMapping("{quizId}/comments")
	@Operation(summary = "View comments on a quiz", description = "Retrieve all user comments on a specific quiz, with support for pagination.")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComments(@PathVariable String quizId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readPageBy(decodeId(quizId), CommentTargetType.QUIZ, search));
	}


	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}
}
