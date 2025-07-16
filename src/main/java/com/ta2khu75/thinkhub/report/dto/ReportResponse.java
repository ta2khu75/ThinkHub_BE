package com.ta2khu75.thinkhub.report.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.report.ReportType;
import com.ta2khu75.thinkhub.report.entity.ReportStatus;
import com.ta2khu75.thinkhub.shared.dto.BaseClassResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse implements BaseClassResponse<ReportIdDto> {
	ReportIdDto id;
	ReportType type;
	Instant createdAt;
	Instant updatedAt;
	AuthorResponse author;
	ReportStatus status;
	Object target;
}
