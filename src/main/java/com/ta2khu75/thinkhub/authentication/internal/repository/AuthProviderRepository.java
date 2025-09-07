package com.ta2khu75.thinkhub.authentication.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authentication.internal.model.AuthProvider;
import com.ta2khu75.thinkhub.authentication.internal.model.ProviderType;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
	Optional<AuthProvider> findByUserIdAndProvider(Long userId, ProviderType provider); // <Tjk>

	Optional<AuthProvider> findByEmailAndProvider(String email, ProviderType provider); // <Tjk
//	boolean existsByEmail(String email);
//
//	@EntityGraph(attributePaths = { "profile", "status" })
//	Optional<Account> findByEmail(String email);
//
//	@EntityGraph(attributePaths = { "profile", "status" })
//	Optional<Account> findByUsername(String username);
}
