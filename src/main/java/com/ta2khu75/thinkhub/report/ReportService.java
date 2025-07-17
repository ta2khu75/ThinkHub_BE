package com.ta2khu75.thinkhub.report;

import com.ta2khu75.thinkhub.report.dto.ReportIdDto;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.dto.ReportStatusRequest;
import com.ta2khu75.thinkhub.report.dto.ReportUpdateRequest;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface ReportService extends SearchService<ReportSearch, ReportResponse> {
	ReportResponse create(Long targetId, ReportTargetType targetType, ReportRequest request);

	ReportResponse read(String targetId, ReportTargetType targetType);

	ReportResponse updateStatus(ReportStatusRequest request);

	ReportResponse update(ReportUpdateRequest request);

	void delete(ReportIdDto id);

}
