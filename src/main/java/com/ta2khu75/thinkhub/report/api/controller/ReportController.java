package com.ta2khu75.thinkhub.report.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.controller.CrudController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Report", description = "Manage user-generated reports for inappropriate or flagged content.")
@ApiController("${app.api-prefix}/reports")
public class ReportController extends BaseController<ReportApi>
		implements CrudController<ReportRequest, ReportResponse, Long> {

	public ReportController(ReportApi service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "Search reports", description = "View a list of reported content submitted by users, with filters and pagination.")
	public ResponseEntity<PageResponse<ReportResponse>> get(@SnakeCaseModelAttribute ReportSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@Override
	@Operation(summary = "Create a report", description = "Permanently create a report from the system.")
	public ResponseEntity<ReportResponse> create(@Valid ReportRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@Override
	@Operation(summary = "Update report details", description = "Modify report metadata, notes, or related internal information.")
	public ResponseEntity<ReportResponse> update(Long id, @Valid ReportRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@PutMapping("status/{id}/{status}")
	@Operation(summary = "Update report status", description = "Change the status of a report, such as marking it as resolved or dismissed.")
	public ResponseEntity<ReportResponse> updateStatus(@PathVariable Long id, @PathVariable ReportStatus status) {
		return ResponseEntity.ok(service.updateStatus(id, status));
	}

	@Override
	@Operation(summary = "Delete a report", description = "Permanently remove a report from the system.")
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Read a report", description = "Permanently read a report from the system.")
	public ResponseEntity<ReportResponse> read(Long id) {
		return ResponseEntity.ok(service.read(id));
	}
}
