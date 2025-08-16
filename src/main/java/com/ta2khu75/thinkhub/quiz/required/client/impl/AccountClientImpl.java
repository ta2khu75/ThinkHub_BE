package com.ta2khu75.thinkhub.quiz.required.client.impl;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.account.api.AccountApi;
import com.ta2khu75.thinkhub.quiz.required.client.AccountClient;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public class AccountClientImpl extends BaseClient<AccountApi> implements AccountClient {

	protected AccountClientImpl(AccountApi api) {
		super(api);
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		return api.readAuthor(id);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByAccountIds(Collection<Long> ids) {
		return api.readMapAuthorsByAccountIds(ids);
	}

}
