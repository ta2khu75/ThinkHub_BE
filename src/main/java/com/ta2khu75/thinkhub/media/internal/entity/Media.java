package com.ta2khu75.thinkhub.media.internal.entity;

import org.springframework.data.annotation.CreatedBy;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Media extends BaseEntityLong {
	@Column(nullable = false, unique = true)
	String filename;
	String url;
	Long size;
	MediaType type;
	@Enumerated(EnumType.STRING)
	MediaOwnerType owner;
	String ownerId;
	@Column(nullable = false)
	@CreatedBy
	String uploadedBy;
}