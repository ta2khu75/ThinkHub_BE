package com.ta2khu75.thinkhub.user.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta2khu75.thinkhub.user.internal.entity.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
	@Query("SELECT u.status FROM User u WHERE u.id = :userId")
	Optional<UserStatus> findByUserId(@Param("userId") Long userId);
}
