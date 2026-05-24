package com.ta2khu75.thinkhub.modules.post.internal.domain;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;
import com.ta2khu75.thinkhub.shared.domain.entity.HasPublicId;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;
import com.ta2khu75.thinkhub.shared.util.SlugUtil;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity implements HasPublicId {
	@Column(nullable = false, length = 255)
	String title;
	@Column(nullable = false, columnDefinition = "TEXT")
	String content;
	@Column(nullable = false)
	String slug;
	int viewCount;
	Long mediaId;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	PostStatus status;
	@ElementCollection
	Set<Long> quizIds;
	@ElementCollection
	@Column(nullable = false)
	Set<Long> tagIds;
	@Column(nullable = false)
	Long categoryId;
	@Column(nullable = false, updatable = false)
	Long ownerId;

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.POST;
	}

	@PrePersist
	public void prePersist() {
		slug = SlugUtil.toSlug(title);
	}

}
