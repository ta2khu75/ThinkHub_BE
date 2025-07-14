package com.ta2khu75.thinkhub.follow.dto;

import java.time.Instant;

import com.ta2khu75.quiz.model.entity.id.FollowId;
import com.ta2khu75.quiz.model.response.account.AccountProfileResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowResponse {
	FollowId id;
	AccountProfileResponse follower;
	AccountProfileResponse following;
	Instant createdAt;
}
