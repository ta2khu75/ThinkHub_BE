package com.ta2khu75.thinkhub.account.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.projection.Author;
import com.ta2khu75.thinkhub.account.request.AccountSearch;

public interface AccountRepositoryCustom {
	Page<Account> search(AccountSearch search);

	Set<Author> findAllAuthorsByAccountIds(Set<Long> accountIds);

	Optional<Author> findAuthorByAccountId(Long accountId);
}