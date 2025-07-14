package com.ta2khu75.thinkhub.report.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.ReportType;
import com.ta2khu75.quiz.model.TargetType;
import com.ta2khu75.quiz.model.entity.Report;
import com.ta2khu75.quiz.model.entity.id.ReportId;

public interface ReportRepository extends JpaRepository<Report, ReportId>{
	@Query("SELECT r FROM Report r WHERE "
			+ "(:authorId IS NULL or r.author.id = :authorId) "
			+ "AND (:targetType IS NULL or r.id.targetType = :targetType) "
			+ "AND (:reportType IS NULL or r.type = :reportType) "
			+ "AND (:reportStatus IS NULL or r.status= :reportStatus) "
			+ "AND (:fromDate IS NULL or r.createdAt >= :fromDate) "
			+ "AND (:toDate IS NULL or r.createdAt <= :toDate) ")
	Page<Report> search(@Param("authorId") Long authorId,@Param("targetType") TargetType targetType, @Param("reportType") ReportType reportType, @Param("reportStatus") ReportStatus reportStatus, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);
}
