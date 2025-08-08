package com.ta2khu75.thinkhub.report.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.report.ReportTargetType;
import com.ta2khu75.thinkhub.report.ReportType;
import com.ta2khu75.thinkhub.report.entity.ReportStatus;
import com.ta2khu75.thinkhub.shared.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportSearch extends Search {
	private ReportTargetType targetType;
	private ReportType reportType;
	private ReportStatus reportStatus;
	private Instant fromDate;
	private Instant toDate;
	private Long authorId;
}
