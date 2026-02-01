package com.ta2khu75.thinkhub.modules.follow.api;

import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

public interface FollowApi {
	void follow(String followingId);

	void unFollow(String followingId);

	FollowStatusResponse isFollowing(String followingId);

	PageResponse<AuthorResponse> readAuthorPage(String followingId, FollowDirection direction, Search search);

	PageResponse<FollowResponse> readPage(String followingId, FollowDirection direction, Search search);
}
