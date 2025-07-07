package com.ta2khu75.thinkhub.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.entity.PermissionGroup;


public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
	PermissionGroup findByName(String name);
}
