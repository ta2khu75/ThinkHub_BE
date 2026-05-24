package com.ta2khu75.thinkhub.modules.follow.internal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.modules.follow.internal.domain.Follow;
import com.ta2khu75.thinkhub.modules.follow.internal.domain.FollowId;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
	Page<Follow> findByIdFollowingId(Long followingId, Pageable pageable);

	Page<Follow> findByIdFollowerId(Long followerId, Pageable pageable);
}
