package com.ta2khu75.thinkhub.modules.user.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserStatusSummary;
import com.ta2khu75.thinkhub.modules.user.api.dto.UserSummary;
import com.ta2khu75.thinkhub.modules.user.internal.entity.User;
import com.ta2khu75.thinkhub.modules.user.internal.entity.UserStatus;
import com.ta2khu75.thinkhub.modules.user.projection.internal.projection.Author;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface UserMapper extends Converter<User, UserResponse>, PageMapper<User, UserResponse> {

	@Override
	@Mapping(target = "id", source = "value")
	UserResponse convert(User value);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "email", ignore = true)
	User toEntity(UserRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "birthday", ignore = true)
	@Mapping(target = "summary", ignore = true)
	User toEntity(UserSummary summary);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "birthday", ignore = true)
	@Mapping(target = "summary", ignore = true)
	User toEntity(UserCreateRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "status", ignore = true)
	void update(UserRequest request, @MappingTarget User entity);

	// Status
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	UserStatus toEntity(UserStatusRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	UserStatus toEntity(UserStatusSummary summary);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	void update(UserStatusRequest request, @MappingTarget UserStatus entity);

	UserStatusResponse toResponse(UserStatus entity);

	UserStatusSummary toSummary(UserStatus entity);

	@Mapping(target = "id", source = "entity")
	UserSummary toSummary(User entity);

	@Mapping(target = "id", source = "entity")
	AuthorResponse toAuthorResponse(Author entity);

}
