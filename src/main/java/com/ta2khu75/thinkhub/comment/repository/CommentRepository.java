package com.ta2khu75.thinkhub.comment.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {
	Page<Comment> findByBlogId(String blogId, Pageable pageable);

	Optional<Comment> findByIdAndAuthorId(String comment, Long authorId);
}
