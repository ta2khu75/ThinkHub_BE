package com.ta2khu75.thinkhub.report.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.report.api.dto.ReportIdDto;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.api.dto.ReportStatusRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportUpdateRequest;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Report", description = "Manage user-generated reports for inappropriate or flagged content.")
@ApiController("${app.api-prefix}/reports")
public class ReportController extends BaseController<ReportApi> {

	public ReportController(ReportApi service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "Search reports", description = "View a list of reported content submitted by users, with filters and pagination.")
	public ResponseEntity<PageResponse<ReportResponse>> get(@SnakeCaseModelAttribute ReportSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PutMapping
	@Operation(summary = "Update report details", description = "Modify report metadata, notes, or related internal information.")
	public ResponseEntity<ReportResponse> update(@Valid @RequestBody ReportUpdateRequest request) {
		return ResponseEntity.ok(service.update(request));
	}

	@PatchMapping("status")
	@Operation(summary = "Update report status", description = "Change the status of a report, such as marking it as resolved or dismissed.")
	public ResponseEntity<ReportResponse> updateStatus(@Valid @RequestBody ReportStatusRequest request) {
		return ResponseEntity.ok(service.updateStatus(request));
	}

	@DeleteMapping
	@Operation(summary = "Delete a report", description = "Permanently remove a report from the system.")
	public ResponseEntity<Void> create(@Valid @RequestBody ReportIdDto request) {
		service.delete(request);
		return ResponseEntity.noContent().build();
	}
}
