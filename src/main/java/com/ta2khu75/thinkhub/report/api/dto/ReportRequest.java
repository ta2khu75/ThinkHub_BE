package com.ta2khu75.thinkhub.report.api.dto;

import com.ta2khu75.thinkhub.report.internal.enums.ReportType;

import jakarta.validation.constraints.NotNull;

public record ReportRequest(@NotNull ReportType type) {
}
