package com.ta2khu75.thinkhub.modules.report.api;

import com.ta2khu75.thinkhub.modules.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.modules.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.modules.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.modules.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface ReportApi
		extends SearchService<ReportSearch, ReportResponse>, CrudService<ReportRequest, ReportResponse, Long> {
	ReportResponse updateStatus(Long id, ReportStatus status);
}
