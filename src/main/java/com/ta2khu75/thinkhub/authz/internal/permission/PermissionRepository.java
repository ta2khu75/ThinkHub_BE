package com.ta2khu75.thinkhub.authz.internal.permission;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMethod;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findBySummary(String summary);
	Set<Permission> findAllBySummaryIn(Set<String> summaries);
	Optional<Permission> findByPatternAndMethod(String pattern, RequestMethod method);
}
