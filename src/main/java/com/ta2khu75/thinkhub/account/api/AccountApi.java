package com.ta2khu75.thinkhub.account.api;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.account.api.dto.AccountDto;
import com.ta2khu75.thinkhub.account.api.dto.UpdatePassword;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountRequest;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountSearch;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountResponse;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface AccountApi extends SearchService<AccountSearch, AccountResponse>, ExistsService<Long> {
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

	void updatePassword(UpdatePassword request);

	AuthorResponse readAuthor(Long id);

	Map<Long,AuthorResponse> readMapAuthorsByAccountIds(Collection<Long> accountIds);
}
