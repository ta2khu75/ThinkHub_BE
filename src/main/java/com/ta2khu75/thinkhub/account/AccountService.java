package com.ta2khu75.thinkhub.account;

import java.util.Set;

import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountSearch;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface AccountService extends SearchService<AccountSearch, AccountResponse>, ExistsService<Long> {
	AccountResponse create(AccountRequest request);

	AccountProfileResponse updateProfile(Long accountId, AccountProfileRequest request);

	AccountStatusResponse updateStatus(Long accountId, AccountStatusRequest request);

	AccountProfileResponse readProfile(Long accountId);

	AccountResponse readByEmail(String email);

	long count();

	void delete(Long id);

	AccountDto readDtoByEmail(String email);

	AccountDto readDtoByUsername(String username);

	AccountDto readDto(Long id);

	void register(RegisterRequest request);

	void changePassword(ChangePasswordRequest request);

	AuthorResponse readAuthor(Long id);
	
	Set<AuthorResponse> readAllAuthorsByAccountIds(Set<Long> accountIds);
}
