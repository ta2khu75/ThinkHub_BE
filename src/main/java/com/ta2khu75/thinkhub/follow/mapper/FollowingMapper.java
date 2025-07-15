package com.ta2khu75.thinkhub.follow.mapper;

import org.mapstruct.Mapping;

import com.ta2khu75.thinkhub.follow.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.entity.Follow;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

public interface FollowingMapper extends PageMapper<Follow, FollowResponse> {
	@Override
	@Mapping(target = "id", source = "id.following")
	FollowResponse convert(Follow source);
}
