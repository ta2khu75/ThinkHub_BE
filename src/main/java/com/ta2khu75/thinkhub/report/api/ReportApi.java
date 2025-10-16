package com.ta2khu75.thinkhub.report.api;

import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;

public interface ReportApi
		extends SearchService<ReportSearch, ReportResponse>, CrudService<ReportRequest, ReportResponse, Long> {
	ReportResponse updateStatus(Long id, ReportStatus status);
}
