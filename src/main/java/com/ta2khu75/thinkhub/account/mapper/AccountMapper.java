package com.ta2khu75.thinkhub.account.mapper;

import org.mapstruct.Mapper;
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
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface AccountMapper extends BaseMapper<AccountRequest, AccountResponse, Account>, PageMapper<Account, AccountResponse>{
	// Profile
	AccountProfile toEntity(AccountProfileRequest request);

	AccountProfileResponse toResponse(AccountProfile entity);

	void update(AccountProfileRequest request, @MappingTarget AccountProfile entity);

	// Status
	AccountStatus toEntity(AccountStatusRequest request);

	AccountStatusResponse toResponse(AccountStatus entity);

	void update(AccountStatusRequest request, @MappingTarget AccountStatus entity);

	AccountResponse toResponse(Account entity);
	
	AccountDto toDto(Account entity);


}
