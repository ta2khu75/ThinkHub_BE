package com.ta2khu75.thinkhub.account.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedBy;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

@Data
@Entity
@ToString(exclude = { "status", "profile", "password" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, exclude = { "status", "profile" })
public class Account extends BaseEntityLong implements IdConfigProvider {
	@Column(unique = true, nullable = false, updatable = false)
	String username;
	@Column(unique = true, nullable = false)
	String email;
	@Column(nullable = false)
	String password;
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	AccountStatus status;
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	AccountProfile profile;
	@CreatedBy
	String createdBy;

	@PrePersist
	@PreUpdate
	public void lowercase() {
		if (username != null) {
			username = username.toLowerCase();
		}
		if (email != null) {
			email = email.toLowerCase();
		}
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.ACCOUNT;
	}

}
