package com.ta2khu75.thinkhub.modules.authProvider.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.modules.authProvider.internal.domain.AuthProvider;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface AuthProviderMapper extends Converter<AuthProvider, AuthProviderSummary> {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "providerId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "userId", ignore = true)
	AuthProvider toEntity(AuthProviderLocal local);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "userId", ignore = true)
	AuthProvider toEntity(AuthProviderOAuth2 oAuth2);

	@Override
	AuthProviderSummary convert(AuthProvider source);
}
