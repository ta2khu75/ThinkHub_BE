package com.ta2khu75.thinkhub.user.internal.entity;

import java.time.Instant;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatus {
	public UserStatus() {
		this.nonLocked = true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	boolean enabled;
	boolean nonLocked;
	boolean deleted;
	@LastModifiedDate
	@Column(insertable = false)
	Instant updatedAt;
	@LastModifiedBy
	@Column(insertable = false)
	String updatedBy;
	@Column(nullable = false)
	Long roleId;
}
