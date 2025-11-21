package com.ta2khu75.thinkhub.authProvider.internal.entity;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthProvider extends BaseEntityLong {
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ProviderType type;
	String providerId;
	@Column(nullable = false)
	String email;
	String password;
	@Column(nullable = false)
	Long userId;

	@PrePersist
	@PreUpdate
	public void lowercase() {
		if (email != null) {
			email = email.toLowerCase();
		}
	}
}
