package com.ta2khu75.thinkhub.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.account.dto.AccountDto;
import com.ta2khu75.thinkhub.account.dto.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.dto.request.AccountRequest;
import com.ta2khu75.thinkhub.account.dto.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.dto.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.dto.response.AccountResponse;
import com.ta2khu75.thinkhub.account.dto.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.entity.AccountProfile;
import com.ta2khu75.thinkhub.account.entity.AccountStatus;
import com.ta2khu75.thinkhub.account.projection.Author;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface AccountMapper extends Converter<Account, AccountResponse>, BaseMapper<AccountRequest, Account>,
		PageMapper<Account, AccountResponse> {
	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	Account toEntity(AccountRequest request);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	void update(AccountRequest request, @MappingTarget Account entity);

//	@Override
//	@Mapping(target = "id", source = "entity")
//	AccountResponse convert(Account entity);

	// Profile
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	AccountProfile toEntity(AccountProfileRequest request);

	AccountProfileResponse toResponse(AccountProfile entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void update(AccountProfileRequest request, @MappingTarget AccountProfile entity);

	// Status
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	AccountStatus toEntity(AccountStatusRequest request);

	AccountStatusResponse toResponse(AccountStatus entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	void update(AccountStatusRequest request, @MappingTarget AccountStatus entity);

	@Mapping(target = "id", source = "entity")
	AccountDto toDto(Account entity);

	@Mapping(target = "id", source = "entity")
	AuthorResponse toAuthorResponse(Author entity);

}
