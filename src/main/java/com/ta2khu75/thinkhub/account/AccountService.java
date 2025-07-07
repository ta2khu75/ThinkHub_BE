package com.ta2khu75.thinkhub.account;

import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountSearch;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface AccountService extends SearchService<AccountResponse, AccountSearch>{
	AccountResponse create(AccountRequest request);
	AccountProfileResponse updateProfile(String accountId,AccountProfileRequest request);
    AccountStatusResponse updateStatus(String accountId,AccountStatusRequest request);
    AccountProfileResponse readProfile(Long id);
    AccountResponse readByEmail(String email);
    long count();
	void delete(String id);
	AccountDto readDtoByEmail(String email);
	AccountDto readDto(String id);
	AccountResponse register(RegisterRequest request);
}
