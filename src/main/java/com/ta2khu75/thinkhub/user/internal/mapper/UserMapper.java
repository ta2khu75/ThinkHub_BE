package com.ta2khu75.thinkhub.user.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.user.internal.entity.User;
import com.ta2khu75.thinkhub.user.internal.entity.UserStatus;
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

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
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "username", ignore = true)
	User toEntity(CreateUserRequest request);

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
	void update(UserStatusRequest request, @MappingTarget UserStatus entity);

	UserStatusResponse toResponse(UserStatus entity);

	@Mapping(target = "id", source = "entity")
	UserDto toDto(User entity);

	@Mapping(target = "id", source = "entity")
	AuthorResponse toAuthorResponse(Author entity);

}
