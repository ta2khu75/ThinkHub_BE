package com.ta2khu75.thinkhub.user.internal.entity;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntityLong implements IdConfigProvider {
	@Column(nullable = false, unique = true)
	String email;
	@Column(nullable = false)
	String username;
	@Column(nullable = false)
	String firstName;
	@Column(nullable = false)
	String lastName;
	LocalDate birthday;
	String summary;
	@LastModifiedDate
	@Column(insertable = false)
	Instant updatedAt;
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	UserStatus status;
	@CreatedBy
	String createdBy;

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@PreUpdate
	@PrePersist
	public void lowercase() {
		if (username != null) {
			username = username.toLowerCase();
		}
		if (email != null) {
			email = email.toLowerCase();
		}
	}

}
