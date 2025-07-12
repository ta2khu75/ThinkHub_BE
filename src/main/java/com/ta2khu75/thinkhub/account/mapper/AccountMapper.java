package com.ta2khu75.thinkhub.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.entity.AccountProfile;
import com.ta2khu75.thinkhub.account.entity.AccountStatus;
import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;
import com.ta2khu75.thinkhub.shared.mapper.IdMapper;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class, uses = IdMapper.class)
public interface AccountMapper
		extends BaseMapper<AccountRequest, AccountResponse, Account>, PageMapper<Account, AccountResponse> {
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
	AccountStatus toEntity(AccountStatusRequest request);

	AccountStatusResponse toResponse(AccountStatus entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	void update(AccountStatusRequest request, @MappingTarget AccountStatus entity);

	@Mapping(target = "id", source = "entity", qualifiedByName = "encodeId")
	AccountResponse toResponse(Account entity);

	@Mapping(target = "id", source = "entity", qualifiedByName = "encodeId")
	AccountDto toDto(Account entity);
}
