package com.ta2khu75.thinkhub.account.entity;

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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountStatus {
	public AccountStatus() {
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
	Long roleId;
}
