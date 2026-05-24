package com.ta2khu75.thinkhub.modules.follow.api.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ta2khu75.thinkhub.modules.follow.internal.domain.Follow;
import com.ta2khu75.thinkhub.modules.follow.internal.repository.FollowRepository;

public enum FollowDirection {
	FOLLOWING {
		@Override
		public Page<Follow> query(FollowRepository repo, Long userId, Pageable pageable) {
			return repo.findByIdFollowingId(userId, pageable);
		}

		@Override
		public Long extractUserId(Follow follow) {
			return follow.getId().getFollowerId();
		}

	},
	FOLLOWER {

		@Override
		public Page<Follow> query(FollowRepository repo, Long userId, Pageable pageable) {
			return repo.findByIdFollowerId(userId, pageable);
		}

		@Override
		public Long extractUserId(Follow follow) {
			return follow.getId().getFollowingId();
		}

	};

	public abstract Page<Follow> query(FollowRepository repo, Long userId, Pageable pageable);

	public abstract Long extractUserId(Follow follow);
}
