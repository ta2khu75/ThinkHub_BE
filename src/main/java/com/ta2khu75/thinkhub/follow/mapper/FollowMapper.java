package com.ta2khu75.thinkhub.follow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.ta2khu75.quiz.model.entity.Follow;
import com.ta2khu75.quiz.model.response.FollowResponse;
import com.ta2khu75.quiz.model.response.PageResponse;

@Mapper(componentModel = "spring", uses = { AccountMapper.class})
public interface FollowMapper  extends PageMapper<Follow, FollowResponse> {
	
	@Mapping(target = "follower", source = "follower", qualifiedByName = "toProfileResponse")
	@Mapping(target = "following", source = "following", qualifiedByName = "toProfileResponse")
	FollowResponse toResponse(Follow entity);

//	@Mapping(target = "page", source = "number")
//	PageResponse<FollowResponse> toPageResponse(Page<Follow> page);
}
