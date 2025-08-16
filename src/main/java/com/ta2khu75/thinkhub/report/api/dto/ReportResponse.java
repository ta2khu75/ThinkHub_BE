package com.ta2khu75.thinkhub.report.api.dto;

import java.time.Instant;

import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.report.internal.enums.ReportType;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;
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
