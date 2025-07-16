package com.ta2khu75.thinkhub.report.dto;

import com.ta2khu75.thinkhub.report.ReportType;

import jakarta.validation.constraints.NotNull;

public record ReportRequest(@NotNull ReportType type) {
}
