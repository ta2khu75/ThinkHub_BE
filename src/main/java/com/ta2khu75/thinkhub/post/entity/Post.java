package com.ta2khu75.thinkhub.post.entity;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Post extends BaseEntityLong implements IdConfigProvider {
	public Post() {
		super();
		this.accessModifier = AccessModifier.PRIVATE;
	}

	@Column(nullable = false, length = 255)
	String title;
	@Column(nullable = false, columnDefinition = "TEXT")
	String content;
	int viewCount;
	boolean deleted;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	AccessModifier accessModifier;
	Set<Long> quizIds;
	Set<Long> tagIds;
	@Column(nullable = false)
	Long categoryId;
	@Column(nullable = false, updatable = false)
	Long authorId;

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.POST;
	}

}
