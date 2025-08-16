package com.ta2khu75.thinkhub.result.internal.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.result.internal.entity.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
	Optional<QuizResult> findByAccountIdAndQuizIdAndEndTimeAfterAndUpdatedAtIsNull(Long accountId, Long quizId, Instant now);
//	Optional<QuizResult> findByAccountIdAndQuizIdAndEndTimeAfterAndUpdatedAtIsNull(Long id, Long quizId,
//			Instant now);
//
//	List<QuizResult> findByEndTimeBeforeAndUpdatedAtIsNull(Instant now);
//	@Query("SELECT q FROM QuizResult q WHERE "
//			+ "(:keyword IS NULL OR q.quiz.title LIKE %:keyword%) "
//			+ "AND (:quizCategoryIds IS NULL OR q.quiz.category.id IN (:quizCategoryIds)) "
//			+ "AND (:accountId IS NULL OR q.account.id= :accountId) "
//			+ "AND (:fromDate IS NULL OR q.updatedAt >= :fromDate) AND (:toDate IS NULL OR q.updatedAt <= :toDate) ")
//	Page<QuizResult> search(@Param("keyword") String keyword, 
//			@Param("quizCategoryIds") Set<Long> quizCategoryIds,
//			@Param("accountId") Long accountId,
//			@Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate,
//			Pageable pageable);
//	Page<ExamResult> findByAccountIdAndUpdatedAtIsNotNull(String accountId, Pageable pageable);
}
