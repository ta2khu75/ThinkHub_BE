package com.ta2khu75.thinkhub.follow.api;

import com.ta2khu75.thinkhub.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public interface FollowApi {
	void follow(String followingId);

	void unFollow(String followingId);

	FollowStatusResponse isFollowing(String followingId);

	PageResponse<AuthorResponse> readAuthorPage(String followingId, FollowDirection direction, Search search);

	PageResponse<FollowResponse> readPage(String followingId, FollowDirection direction, Search search);
}
