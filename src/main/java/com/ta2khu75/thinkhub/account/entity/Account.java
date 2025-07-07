package com.ta2khu75.thinkhub.account.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityString;

@Data
@Entity
@ToString(exclude = { "status", "profile", "password" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true, exclude = { "status", "profile" })
public class Account extends BaseEntityString {
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
}
