package com.ta2khu75.thinkhub.post.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.post.internal.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
//	List<Post> findAllByAuthorIdAndTitleContainingIgnoreCase(Long authorId, String keyword);
//
//	Long countByAuthorIdAndAccessModifier(Long authorId, AccessModifier accessModifier);
//
//	Optional<Post> findByIdAndAuthorId(Long blogId, Long authorId);
}
