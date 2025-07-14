package com.ta2khu75.thinkhub.report.dto;

import java.time.Instant;

import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.base.ReportBase;
import com.ta2khu75.quiz.model.dto.ReportIdDto;
import com.ta2khu75.quiz.model.entity.id.ReportId;
import com.ta2khu75.quiz.model.response.account.AccountProfileResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse extends ReportBase implements BaseResponse<ReportIdDto> {
	ReportIdDto id;
	Instant createdAt;
	Instant updatedAt;
	AccountProfileResponse author;
	ReportStatus status;
	Object target;
}
