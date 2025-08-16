package com.ta2khu75.thinkhub.report.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Report", description = "Manage user-generated reports for inappropriate or flagged content.")
@ApiController("${app.api-prefix}/comments")
public class CommentReportController extends BaseController<ReportApi> {

	protected CommentReportController(ReportApi service) {
		super(service);
	}

	@PostMapping("{commentId}/reports")
	@Operation(summary = "Report a comment", description = "Flag a comment as inappropriate or offensive for moderation.")
	public ResponseEntity<ReportResponse> report(@PathVariable Long id, @RequestBody ReportRequest content) {
		return ResponseEntity.ok(service.create(id, ReportTargetType.COMMENT, content));
	}
}
