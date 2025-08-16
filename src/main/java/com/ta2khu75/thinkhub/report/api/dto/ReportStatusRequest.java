package com.ta2khu75.thinkhub.report.api.dto;

import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReportStatusRequest(@NotNull @Valid ReportIdDto id, @NotNull ReportStatus status) {
}
