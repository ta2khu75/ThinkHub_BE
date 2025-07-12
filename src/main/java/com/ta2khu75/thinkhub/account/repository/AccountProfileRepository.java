package com.ta2khu75.thinkhub.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ta2khu75.thinkhub.account.entity.AccountProfile;

import io.lettuce.core.dynamic.annotation.Param;

public interface AccountProfileRepository extends JpaRepository<AccountProfile, Long> {
	@Query("SELECT a.profile FROM Account a WHERE a.id = :accountId")
	Optional<AccountProfile> findByAccountId(@Param("accountId") Long accountId);
}
