package com.ta2khu75.thinkhub.report.api.event;

import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;

public record ReportCreatedEvent(Long targetId, ReportTargetType targetType) {

}
