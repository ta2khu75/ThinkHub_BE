package com.ta2khu75.thinkhub.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Permission findBySummary(String summary);
}
