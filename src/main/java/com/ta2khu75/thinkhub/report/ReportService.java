package com.ta2khu75.thinkhub.report;

import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.entity.id.ReportId;
import com.ta2khu75.quiz.model.request.ReportRequest;
import com.ta2khu75.quiz.model.request.search.ReportSearch;
import com.ta2khu75.quiz.model.request.update.ReportStatusRequest;
import com.ta2khu75.quiz.model.response.ReportResponse;
import com.ta2khu75.quiz.service.base.CrudService;
import com.ta2khu75.quiz.service.base.SearchService;

public interface ReportService extends  SearchService<ReportResponse, ReportSearch> {
	ReportResponse updateStatus(ReportStatusRequest request);
	ReportResponse create(ReportRequest request);
	ReportResponse update(ReportRequest request);
//	ReportResponse read(String id);
//	void delete(String id);
}
