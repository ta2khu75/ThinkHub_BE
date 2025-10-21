package com.ta2khu75.thinkhub.category.internal.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.util.SlugUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntityLong {
	@Column(nullable = false, unique = true)
	String name;
	@Column(nullable = false)
	String slug;
	String description;
	@Column(nullable = false)
	Long mediaId;
	@Column(nullable = false)
	Long defaultMediaId;
	@CreatedBy
	@Column(updatable = false, nullable = false)
	String createdBy;
	@Column(insertable = false)
	@LastModifiedBy
	String updatedBy;

	@PreUpdate
	public void normalizeName() {
		if (name != null) {
			name = name.trim().toLowerCase(); // chuẩn hóa
		}
	}

	@PrePersist
	public void makeSlug() {
		slug = SlugUtil.toSlug(name);
		if (name != null) {
			name = name.trim().toLowerCase(); // chuẩn hóa
		}
	}
}
