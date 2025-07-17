package com.ta2khu75.thinkhub.notification.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.notification.NotificationStatus;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityCustom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntityCustom<NotificationId> {

	public Notification() {
		super();
		status = NotificationStatus.UNREAD;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	NotificationStatus status = NotificationStatus.UNREAD;
}
