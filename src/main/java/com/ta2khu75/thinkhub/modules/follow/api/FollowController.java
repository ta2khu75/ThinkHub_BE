package com.ta2khu75.thinkhub.modules.follow.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@ApiController("${app.api-prefix}/follows")
@Tag(name = "Follow")
public class FollowController extends BaseController<FollowApi> implements IdDecodable {
	protected FollowController(FollowApi service) {
		super(service);
	}

	@PostMapping("{accountId}")
	@Operation(summary = "Follow a user", description = "Start following a specific user account.")
	public ResponseEntity<Void> follow(@PathVariable String userId) {
		service.follow(userId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("{accountId}")
	@Operation(summary = "Unfollow a user", description = "Stop following a previously followed user account.")
	public ResponseEntity<Void> unfollow(@PathVariable String userId) {
		service.unFollow(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{accountId}/status")
	@Operation(summary = "Check follow status", description = "Check whether you are currently following the specified user.")
	public ResponseEntity<FollowStatusResponse> isFollowing(@PathVariable String userId) {
		return ResponseEntity.ok(service.isFollowing(userId));
	}

	@GetMapping("{accountId}/followers")
	@Operation(summary = "Get followers", description = "View the list of users who are currently following the specified account.")
	public ResponseEntity<PageResponse<AuthorResponse>> readFollowers(@PathVariable String userId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readAuthorPage(userId, FollowDirection.FOLLOWER, search));
	}

	@GetMapping("{accountId}/following")
	@Operation(summary = "Get following", description = "View the list of accounts the user is currently following.")
	public ResponseEntity<PageResponse<AuthorResponse>> readFollowing(@PathVariable String userId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readAuthorPage(userId, FollowDirection.FOLLOWING, search));
	}

	@Override
	public IdConfig getIdConfig() {
		return null;
	}

}
