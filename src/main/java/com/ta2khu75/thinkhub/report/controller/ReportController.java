package com.ta2khu75.thinkhub.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ta2khu75.thinkhub.report.ReportService;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.dto.ReportStatusRequest;
import com.ta2khu75.thinkhub.report.dto.ReportUpdateRequest;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;

import jakarta.validation.Valid;

@Controller
@RequestMapping("${app.api-prefix}/reports")
public class ReportController extends BaseController<ReportService> {

	public ReportController(ReportService service) {
		super(service);
	}

	@GetMapping
	public ResponseEntity<PageResponse<ReportResponse>> get(@SnakeCaseModelAttribute ReportSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PatchMapping
	public ResponseEntity<ReportResponse> update(@Valid @RequestBody ReportUpdateRequest request) {
		return ResponseEntity.ok(service.update(request));
	}

	@PatchMapping("status")
	public ResponseEntity<ReportResponse> updateStatus(@Valid @RequestBody ReportStatusRequest request) {
		return ResponseEntity.ok(service.updateStatus(request));
	}
}
