package com.ta2khu75.thinkhub.report.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.report.internal.enums.ReportType;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

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
