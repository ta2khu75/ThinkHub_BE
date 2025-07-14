package com.ta2khu75.thinkhub.follow;

import org.springframework.data.domain.Pageable;

import com.ta2khu75.quiz.model.response.FollowResponse;
import com.ta2khu75.quiz.model.response.PageResponse;

public interface FollowService {
	FollowResponse create(Long followingId);

	void delete(Long followingId);

	FollowResponse read(Long followingId);

	PageResponse<FollowResponse> readPage(Long followingId, Pageable pageable);
}
