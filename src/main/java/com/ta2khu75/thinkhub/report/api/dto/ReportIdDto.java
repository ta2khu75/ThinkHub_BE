package com.ta2khu75.thinkhub.report.api.dto;

import java.io.Serializable;

import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;

import jakarta.validation.constraints.NotNull;

public record ReportIdDto(@NotNull String authorId, @NotNull String targetId, @NotNull ReportTargetType targetType)
		implements Serializable {

}
