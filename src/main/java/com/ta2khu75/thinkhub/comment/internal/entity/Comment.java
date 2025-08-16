package com.ta2khu75.thinkhub.comment.internal.entity;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	Long authorId;
	CommentTargetType targetType;
	Long targetId;
}
