package com.ta2khu75.thinkhub.report.dto;

import java.io.Serializable;

import com.ta2khu75.thinkhub.report.ReportTargetType;

import jakarta.validation.constraints.NotNull;

public record ReportIdDto(@NotNull String authorId, @NotNull String targetId, @NotNull ReportTargetType targetType)
		implements Serializable {

}
