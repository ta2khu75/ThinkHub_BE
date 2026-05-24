package com.ta2khu75.thinkhub.modules.follow.internal.mapper;

import org.mapstruct.Mapping;

import com.ta2khu75.thinkhub.modules.follow.api.dto.FollowResponse;
import com.ta2khu75.thinkhub.modules.follow.internal.domain.Follow;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

public interface FollowerMapper extends PageMapper<Follow, FollowResponse> {
	@Override
	@Mapping(target = "id", source = "id.followerId")
	FollowResponse convert(Follow source);
}
