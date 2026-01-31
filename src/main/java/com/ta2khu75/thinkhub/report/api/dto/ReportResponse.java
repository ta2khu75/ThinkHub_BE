package com.ta2khu75.thinkhub.report.api.dto;

import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.report.internal.enums.ReportType;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.BaseClassResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse extends BaseClassResponse<Long> {
	ReportType type;
	AuthorResponse author;
	ReportStatus status;
	Object target;
}
