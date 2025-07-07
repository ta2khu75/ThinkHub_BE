package com.ta2khu75.thinkhub.authority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.authority.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@EntityGraph(attributePaths = { "permissions" })
	Optional<Role> findByName(String name);
}
