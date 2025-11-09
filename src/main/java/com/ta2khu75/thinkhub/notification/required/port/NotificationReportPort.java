package com.ta2khu75.thinkhub.notification.required.port;

import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;

public interface NotificationReportPort {
	ReportResponse read(Long id);
}
