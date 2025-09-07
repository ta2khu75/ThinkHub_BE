package com.ta2khu75.thinkhub.follow.api;

import com.ta2khu75.thinkhub.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.api.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public interface FollowApi {
	void follow(Long followingId);

	void unFollow(Long followingId);

	FollowStatusResponse isFollowing(Long followingId);

	PageResponse<AuthorResponse> readAuthorPage(Long followingId, FollowDirection direction, Search search);
	PageResponse<FollowResponse> readPage(Long followingId, FollowDirection direction, Search search);	
}
