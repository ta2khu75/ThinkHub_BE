package com.ta2khu75.thinkhub.notification.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.notification.required.port.NotificationReportPort;
import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;

@Component
class NotificationReportClient extends BaseClient<ReportApi> implements NotificationReportPort {

	protected NotificationReportClient(ReportApi api) {
		super(api);
	}

	@Override
	public ReportResponse read(Long id) {
		return api.read(id);
	}

}
