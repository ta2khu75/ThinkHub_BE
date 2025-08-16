package com.ta2khu75.thinkhub.account.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta2khu75.thinkhub.account.internal.entity.AccountStatus;

public interface AccountStatusRepository extends JpaRepository<AccountStatus, Long> {
	@Query("SELECT a.status FROM Account a WHERE a.id = :accountId")
	Optional<AccountStatus> findByAccountId(@Param("accountId") Long accountId);
}
