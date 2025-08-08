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
@ApiController("${app.api-prefix}/posts")
public class PostCommentController extends BaseController<CommentService> implements IdDecodable{
	
	protected PostCommentController(CommentService service) {
		super(service);
	}


	@PostMapping("{postId}/comments")
	@Operation(summary = "Add a comment to a post", description = "Post a comment on a specific post to start or join a discussion.")
	public ResponseEntity<CommentResponse> comment(@PathVariable String postId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(decodeId(postId), CommentTargetType.POST, request));
	}
	@GetMapping("{postId}/comments")
	@Operation(summary = "Get comments on a post", description = "View all comments under a specific post, with pagination support.")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComments(@PathVariable String postId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readPageBy(decodeId(postId), CommentTargetType.POST, search));
	}


	@Override
	public IdConfig getIdConfig() {
		return IdConfig.POST;
	}
}
