package com.ta2khu75.thinkhub.report.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.report.internal.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	
//	@Query("SELECT r FROM Report r WHERE "
//			+ "(:authorId IS NULL or r.author.id = :authorId) "
//			+ "AND (:targetType IS NULL or r.id.targetType = :targetType) "
//			+ "AND (:reportType IS NULL or r.type = :reportType) "
//			+ "AND (:reportStatus IS NULL or r.status= :reportStatus) "
//			+ "AND (:fromDate IS NULL or r.createdAt >= :fromDate) "
//			+ "AND (:toDate IS NULL or r.createdAt <= :toDate) ")
//	Page<Report> search(@Param("authorId") Long authorId,@Param("targetType") CommentTarget targetType, @Param("reportType") ReportType reportType, @Param("reportStatus") ReportStatus reportStatus, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);
}
