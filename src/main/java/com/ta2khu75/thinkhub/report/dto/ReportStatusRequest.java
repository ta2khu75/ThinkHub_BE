package com.ta2khu75.thinkhub.report.dto;

import com.ta2khu75.thinkhub.report.entity.ReportStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReportStatusRequest(@NotNull @Valid ReportIdDto id, @NotNull ReportStatus status) {
}
