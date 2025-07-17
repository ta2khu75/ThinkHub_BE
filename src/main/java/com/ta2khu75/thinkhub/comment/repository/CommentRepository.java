package com.ta2khu75.thinkhub.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByTargetIdAndTargetType(Long targetId, CommentTargetType targetType, Pageable pageable);
}
