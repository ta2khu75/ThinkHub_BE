package com.ta2khu75.thinkhub.authz.internal.permission;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByCode(String code);

	List<Permission> findAllByCodeIn(Iterable<String> codes);
}
