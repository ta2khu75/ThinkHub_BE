package com.ta2khu75.thinkhub.follow.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.quiz.model.entity.id.FollowId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Follow {
	@Id
	@EmbeddedId
	FollowId id;
	@ManyToOne
	@MapsId("followerId")
	AccountProfile follower;
	@ManyToOne
	@MapsId("followingId")
	AccountProfile following;
	@CreatedDate
	@Column(updatable = false, nullable = false)
	Instant createdAt;
	}