package com.ta2khu75.thinkhub.report.api;

import com.ta2khu75.thinkhub.report.api.dto.ReportIdDto;
import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.api.dto.ReportStatusRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportUpdateRequest;
import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface ReportApi extends SearchService<ReportSearch, ReportResponse> {
	ReportResponse create(Long targetId, ReportTargetType targetType, ReportRequest request);

	ReportResponse read(String targetId, ReportTargetType targetType);

	ReportResponse updateStatus(ReportStatusRequest request);

	ReportResponse update(ReportUpdateRequest request);

	void delete(ReportIdDto id);

}
