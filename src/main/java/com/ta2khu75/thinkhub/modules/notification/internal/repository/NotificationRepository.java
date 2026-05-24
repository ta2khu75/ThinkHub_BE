package com.ta2khu75.thinkhub.modules.notification.internal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.modules.notification.internal.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Page<Notification> findByUserId(Long userId, Pageable pageable);
}
