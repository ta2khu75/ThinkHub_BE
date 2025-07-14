package com.ta2khu75.thinkhub.report.dto;

import java.time.Instant;

import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.ReportType;
import com.ta2khu75.quiz.model.TargetType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportSearch extends Search {
	private TargetType targetType;
	private ReportType reportType;
	private ReportStatus reportStatus;
	private Instant fromDate;
	private Instant toDate;
	private Long authorId;
}
