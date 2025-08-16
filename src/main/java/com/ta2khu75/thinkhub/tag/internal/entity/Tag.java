package com.ta2khu75.thinkhub.tag.internal.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, unique = true)
	String name;
	@CreatedDate
	@Column(nullable = false)
	Instant createdAt;

	@PrePersist
	@PreUpdate
	public void normalizeName() {
		if (name != null) {
			name = name.trim().toLowerCase(); // chuẩn hóa
		}
	}

	public Tag(String name) {
		super();
		this.name = name;
	}

}
