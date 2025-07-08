package com.ta2khu75.thinkhub.authority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.entity.PermissionGroup;


public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
	Optional<PermissionGroup> findByName(String name);
}
