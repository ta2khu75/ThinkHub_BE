package com.ta2khu75.thinkhub.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
	boolean existsByEmail(String email);

	@EntityGraph(attributePaths = { "profile", "status" })
	Optional<Account> findByEmail(String email);

	@EntityGraph(attributePaths = { "profile", "status" })
	Optional<Account> findByUsername(String username);
}
