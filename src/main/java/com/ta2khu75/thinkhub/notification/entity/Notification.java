package com.ta2khu75.thinkhub.notification.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.quiz.model.NotificationStatus;
import com.ta2khu75.quiz.model.entity.base.BaseEntityCustom;
import com.ta2khu75.quiz.model.entity.id.NotificationId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntityCustom<NotificationId> {
	@ManyToOne
	@MapsId("accountId")
	AccountProfile account;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	NotificationStatus status=NotificationStatus.UNREAD;
}
