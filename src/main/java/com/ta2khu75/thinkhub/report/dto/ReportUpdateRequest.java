package com.ta2khu75.thinkhub.report.dto;

import com.ta2khu75.thinkhub.report.ReportType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReportUpdateRequest(@NotNull @Valid ReportIdDto id, @NotNull ReportType type) {

}
