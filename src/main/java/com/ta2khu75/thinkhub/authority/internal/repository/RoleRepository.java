package com.ta2khu75.thinkhub.authority.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.internal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@EntityGraph(attributePaths = { "permissions" })
	Optional<Role> findByName(String name);
	boolean existsByName(String name);
}
