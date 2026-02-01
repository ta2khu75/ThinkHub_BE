package com.ta2khu75.thinkhub.modules.report.api.dto;

import com.ta2khu75.thinkhub.modules.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.modules.report.internal.enums.ReportType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(@NotBlank String targetId, @NotNull ReportTargetType targetType, @NotNull ReportType type) {
}
