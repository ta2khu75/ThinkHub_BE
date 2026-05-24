package com.ta2khu75.thinkhub.modules.category.internal.domain;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.ta2khu75.thinkhub.modules.category.api.model.CategoryStatus;
import com.ta2khu75.thinkhub.modules.category.internal.validator.CategoryErrorCode;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;
import com.ta2khu75.thinkhub.shared.util.Guard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@SQLRestriction("status <> 'DELETED'")
public class Category extends BaseEntity {
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	String slug;
	String description;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	CategoryStatus status;
	@Column(nullable = false)
	Long imageId;
	@Column(nullable = false)
	Long fallbackImageId;
	@CreatedBy
	@Column(updatable = false, nullable = false)
	Long createdBy;
	@Column(insertable = false)
	@LastModifiedBy
	Long updatedBy;

	protected Category() {
	}

	public static Category create(String name, String slug, String description, Long imageId, Long fallbackImageId) {
		Guard.notBlank(name, CategoryErrorCode.INVALID_NAME, "name");
		Guard.notBlank(slug, CategoryErrorCode.INVALID_SLUG, "slug");
		validateImages(imageId, fallbackImageId);
		Category category = new Category();
		category.name = name;
		category.slug = slug;
		category.description = description;
		category.imageId = imageId;
		category.fallbackImageId = fallbackImageId;
		category.status = CategoryStatus.INACTIVE;
		return category;
	}

	public void activate() {
		Guard.state(this.status == CategoryStatus.INACTIVE, CategoryErrorCode.INVALID_STATE,
				"Only inactive category can be activated");
		Guard.notNull(imageId, CategoryErrorCode.INVALID_IMAGE, "imageId required to activate");
		Guard.notNull(fallbackImageId, CategoryErrorCode.INVALID_IMAGE, "fallbackImageId required to activate");
		this.status = CategoryStatus.ACTIVE;
	}

	public void deactivate() {
		Guard.state(this.status == CategoryStatus.ACTIVE, CategoryErrorCode.INVALID_STATE,
				"Only active category can be deactivated");
		this.status = CategoryStatus.INACTIVE;
	}

	public void update(String name, String slug, String description, Long imageId, Long fallbackImageId) {
		updateInfo(name, slug, description);
		changeImage(imageId, fallbackImageId);
	}

	public void updateInfo(String name, String slug, String description) {
		ensureNotDeleted();
		Guard.notBlank(name, CategoryErrorCode.INVALID_NAME, "name");
		Guard.notBlank(slug, CategoryErrorCode.INVALID_SLUG, "slug");
		this.name = name;
		this.slug = slug;
		this.description = description;
	}

	public void changeImage(Long imageId, Long fallbackImageId) {
		ensureNotDeleted();
		validateImages(imageId, fallbackImageId);
		this.imageId = imageId;
		this.fallbackImageId = fallbackImageId;
	}

	public void delete() {
		Guard.state(this.status == CategoryStatus.INACTIVE, CategoryErrorCode.INVALID_STATE,
				"Only inactive category can be deleted");
		this.status = CategoryStatus.DELETED;
	}

	private void ensureNotDeleted() {
		Guard.condition(this.status != CategoryStatus.DELETED, CategoryErrorCode.DELETED, "Category deleted");
	}

	private static void validateImages(Long imageId, Long fallbackImageId) {
		Guard.notNull(imageId, CategoryErrorCode.INVALID_IMAGE, "imageId");
		Guard.notNull(fallbackImageId, CategoryErrorCode.INVALID_IMAGE, "fallbackImageId");
		Guard.condition(!imageId.equals(fallbackImageId), CategoryErrorCode.INVALID_IMAGE,
				"Image and fallback image must differ");
	}
}
