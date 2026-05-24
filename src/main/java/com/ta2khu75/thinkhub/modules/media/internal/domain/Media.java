package com.ta2khu75.thinkhub.modules.media.internal.domain;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;

import com.ta2khu75.thinkhub.modules.media.internal.validator.MediaErrorCode;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;
import com.ta2khu75.thinkhub.shared.exception.BusinessException;
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
public class Media extends BaseEntity {
	protected Media() {
	}

	public static Media create(String filename, String url, Long size, MediaType type) {
		Guard.notBlank(filename, MediaErrorCode.INVALID_FILENAME, "filename");
		Guard.notBlank(url, MediaErrorCode.INVALID_URL, "url");
		Guard.positive(size, MediaErrorCode.INVALID_SIZE, "size");
		Guard.notNull(type, MediaErrorCode.INVALID_TYPE, "type");
		Media media = new Media();
		media.filename = filename;
		media.url = url;
		media.size = size;
		media.type = type;
		media.status = MediaStatus.DRAFT;
		return media;
	}

	@Column(nullable = false)
	String filename;
	@Column(nullable = false, unique = true)
	String url;
	@Column(nullable = false)
	Long size;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	MediaType type;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	MediaStatus status;
	@CreatedBy
	@Column(nullable = false, updatable = false)
	Long createdBy;

	public void attach() {
		assertState(MediaStatus.DRAFT, "attach");
		this.status = MediaStatus.ATTACHED;
	}

	public void detach() {
		assertState(MediaStatus.ATTACHED, "detach");
		this.status = MediaStatus.DRAFT;
	}

	public void markDeleted() {
		assertState(MediaStatus.DRAFT, "delete");
		this.status = MediaStatus.DELETED;
	}

	private void assertState(MediaStatus expected, String action) {
		Guard.condition(this.status == expected, MediaErrorCode.INVALID_STATE,
				"Cannot " + action + " media in status " + status + ". Expected status: " + expected + ".");
	}
}