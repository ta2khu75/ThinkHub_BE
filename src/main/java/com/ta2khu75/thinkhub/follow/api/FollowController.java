package com.ta2khu75.thinkhub.follow.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
@ApiController("${app.api-prefix}/accounts")
public class FollowController extends BaseController<FollowApi> implements IdDecodable{
	protected FollowController(FollowApi service) {
		super(service);
	}

	@PostMapping("{accountId}/follow")
	@Operation(summary = "Follow a user", description = "Start following a specific user account.")
	public ResponseEntity<Void> follow(@PathVariable String accountId) {
		service.follow(this.decodeAccountId(accountId));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("{accountId}/follow")
	@Operation(summary = "Unfollow a user", description = "Stop following a previously followed user account.")
	public ResponseEntity<Void> unfollow(@PathVariable String accountId) {
		service.unFollow(this.decodeAccountId(accountId));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{accountId}/follow/status")
	@Operation(summary = "Check follow status", description = "Check whether you are currently following the specified user.")
	public ResponseEntity<FollowStatusResponse> isFollowing(@PathVariable String accountId) {
		return ResponseEntity.ok(service.isFollowing(this.decodeAccountId(accountId)));
	}

	@GetMapping("{accountId}/followers")
	@Operation(summary = "Get followers", description = "View the list of users who are currently following the specified account.")
	public ResponseEntity<PageResponse<AuthorResponse>> readFollowers(@PathVariable String accountId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readPage(this.decodeAccountId(accountId), FollowDirection.FOLLOWER, search));
	}

	@GetMapping("{accountId}/following")
	@Operation(summary = "Get following", description = "View the list of accounts the user is currently following.")
	public ResponseEntity<PageResponse<AuthorResponse>> readFollowing(@PathVariable String accountId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity
				.ok(service.readPage(this.decodeAccountId(accountId), FollowDirection.FOLLOWING, search));
	}

	@Override
	public IdConfig getIdConfig() {
		return null;
	}
	
}
