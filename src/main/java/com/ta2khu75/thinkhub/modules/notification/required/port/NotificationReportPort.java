package com.ta2khu75.thinkhub.modules.notification.required.port;

import com.ta2khu75.thinkhub.modules.report.api.dto.ReportResponse;

public interface NotificationReportPort {
	ReportResponse read(Long id);
}
