package com.ta2khu75.thinkhub.authProvider.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authProvider.internal.entity.AuthProvider;
import com.ta2khu75.thinkhub.authProvider.internal.entity.ProviderType;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
	Optional<AuthProvider> findByUserIdAndType(Long userId, ProviderType type);

	Optional<AuthProvider> findByEmailAndType(String email, ProviderType type);
}
