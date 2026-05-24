package com.ta2khu75.thinkhub.modules.user.internal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta2khu75.thinkhub.modules.user.internal.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	boolean existsByEmail(String email);

	@EntityGraph(attributePaths = { "status" })
	Optional<User> findByEmail(String email);

	@EntityGraph(attributePaths = { "status" })
	Optional<User> findByUsername(String username);

	@Query("select u.id from User u where u.status.roleId = :roleId")
	List<Long> findAllUserIdByRoleId(@Param("roleId") Long roleId);

}
