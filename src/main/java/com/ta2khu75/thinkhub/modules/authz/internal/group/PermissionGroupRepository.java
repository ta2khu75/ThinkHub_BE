package com.ta2khu75.thinkhub.modules.authz.internal.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
	Optional<PermissionGroup> findByCode(String code);

	List<PermissionGroup> findAllByCodeIn(Iterable<String> codes);
}
