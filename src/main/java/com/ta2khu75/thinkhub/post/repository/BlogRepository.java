package com.ta2khu75.thinkhub.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta2khu75.quiz.model.AccessModifier;
import com.ta2khu75.quiz.model.entity.Blog;
import com.ta2khu75.quiz.repository.custom.BlogRepositoryCustom;

public interface BlogRepository extends JpaRepository<Blog, Long>, BlogRepositoryCustom {
//	@Query("SELECT b FROM Blog b LEFT JOIN b.tags t WHERE" + "(:blogTagNames IS NULL OR t.name IN (:blogTagNames)) "
//			+ "AND (:keyword IS NULL OR b.title LIKE %:keyword% OR b.author.displayName LIKE %:keyword%) "
//			+ "AND (:authorId IS NULL OR b.author.id = :authorId) "
//			+ "AND (:minView IS NULL OR b.viewCount >= :minView) "
//			+ "AND (:maxView IS NULL OR b.viewCount <= :maxView) "
//			+ "AND (:accessModifier IS NULL OR b.accessModifier = :accessModifier)")
//	Page<Blog> search(@Param("blogTagNames") List<String> blogTagNames, @Param("keyword") String keyword,
//			@Param("authorId") Long authorId, @Param("minView") Integer minView, @Param("maxView") Integer maxView,
//			@Param("accessModifier") AccessModifier accessModifier, Pageable pageable);

	List<Blog> findAllByAuthorIdAndTitleContainingIgnoreCase(Long authorId, String keyword);

	Long countByAuthorIdAndAccessModifier(Long authorId, AccessModifier accessModifier);

	Optional<Blog> findByIdAndAuthorId(Long blogId, Long authorId);
}
