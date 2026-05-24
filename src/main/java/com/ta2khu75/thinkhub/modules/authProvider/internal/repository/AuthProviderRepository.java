package com.ta2khu75.thinkhub.modules.authProvider.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.modules.authProvider.api.model.ProviderType;
import com.ta2khu75.thinkhub.modules.authProvider.internal.domain.AuthProvider;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
	Optional<AuthProvider> findByUserIdAndType(Long userId, ProviderType type);

	Optional<AuthProvider> findByEmailAndType(String email, ProviderType type);
}
