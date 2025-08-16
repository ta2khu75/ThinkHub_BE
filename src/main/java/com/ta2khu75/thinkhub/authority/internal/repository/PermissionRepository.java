package com.ta2khu75.thinkhub.authority.internal.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.internal.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findBySummary(String summary);
	Set<Permission> findAllBySummaryIn(Set<String> summaries);
}
