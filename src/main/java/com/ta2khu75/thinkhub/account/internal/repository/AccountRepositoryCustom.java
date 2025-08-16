package com.ta2khu75.thinkhub.account.internal.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.account.api.dto.request.AccountSearch;
import com.ta2khu75.thinkhub.account.internal.entity.Account;
import com.ta2khu75.thinkhub.account.internal.projection.Author;

public interface AccountRepositoryCustom {
	Page<Account> search(AccountSearch search);

	List<Author> findAllAuthorsByAccountIds(Collection<Long> accountIds);

	Optional<Author> findAuthorByAccountId(Long accountId);
}