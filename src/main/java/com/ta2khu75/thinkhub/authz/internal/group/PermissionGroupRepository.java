package com.ta2khu75.thinkhub.authz.internal.group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
	Optional<PermissionGroup> findByName(String name);
}
