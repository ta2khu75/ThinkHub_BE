package com.ta2khu75.thinkhub.comment.entity;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntityLong {
	@Column(nullable = false, columnDefinition = "NVARCHAR(255)")
	String content;
	@ManyToOne
	Long authorId;
	@ManyToOne
	Long blogId;
}
