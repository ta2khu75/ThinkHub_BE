package com.ta2khu75.thinkhub.modules.notification.internal.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.modules.notification.api.model.NotificationStatus;
import com.ta2khu75.thinkhub.modules.notification.api.model.NotificationTarget;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {
	public Notification() {
		super();
		status = NotificationStatus.UNREAD;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	NotificationStatus status;
	@Column(nullable = false)
	private Long userId;
	@Column(nullable = false)
	private String targetId;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationTarget target;
}
