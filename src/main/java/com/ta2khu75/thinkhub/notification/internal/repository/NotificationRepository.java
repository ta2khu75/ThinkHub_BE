package com.ta2khu75.thinkhub.notification.internal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.notification.internal.entity.Notification;
import com.ta2khu75.thinkhub.notification.internal.entity.NotificationId;

public interface NotificationRepository extends JpaRepository<Notification, NotificationId> {
	Page<Notification> findByIdAccountId(Long accountId, Pageable pageable);
}
