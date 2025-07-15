package com.ta2khu75.thinkhub.follow;

import com.ta2khu75.thinkhub.follow.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

public interface FollowService {
	void follow(Long followingId);

	void unFollow(Long followingId);

	FollowStatusResponse isFollowing(Long followingId);

	PageResponse<FollowResponse> readPage(Long followingId, FollowDirection direction, Search search);
}
