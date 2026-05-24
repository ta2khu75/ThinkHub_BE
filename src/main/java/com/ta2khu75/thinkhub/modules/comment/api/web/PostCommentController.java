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
import jakarta.validation.Valid;

@Tag(name = "Comment", description = "Manage user comments and allow reporting of inappropriate content.")
@ApiController("${app.api-prefix}/posts")
public class PostCommentController extends BaseController<CommentApi> {

	protected PostCommentController(CommentApi api) {
		super(api);
	}

	@PostMapping("{postId}/comments")
	@Operation(summary = "Add a comment to a post", description = "Post a comment on a specific post to start or join a discussion.")
	public ResponseEntity<CommentResponse> comment(@PathVariable String postId,
			@Valid @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(api.create(postId, CommentTargetType.POST, request));
	}

	@GetMapping("{postId}/comments")
	@Operation(summary = "Get comments on a post", description = "View all comments under a specific post, with pagination support.")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComments(@PathVariable String postId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(api.readPageBy(postId, CommentTargetType.POST, search));
	}

}
