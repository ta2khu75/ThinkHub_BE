package com.ta2khu75.thinkhub.account;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountSearch;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.follow.FollowDirection;
import com.ta2khu75.thinkhub.follow.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
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

	void changePassword(ChangePasswordRequest request);

	AuthorResponse readAuthor(Long id);

	Map<Long,AuthorResponse> readMapAuthorsByAccountIds(Collection<Long> accountIds);

	void follow(Long followingId);

	void unFollow(Long followingId);

	FollowStatusResponse isFollowing(Long followingId);

	PageResponse<AuthorResponse> readFollow(Long following, FollowDirection direction, Search search);
}
