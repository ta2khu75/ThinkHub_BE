package com.ta2khu75.thinkhub.report.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.report.ReportService;
import com.ta2khu75.thinkhub.report.ReportTargetType;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Report", description = "Manage user-generated reports for inappropriate or flagged content.")
@ApiController("${app.api-prefix}/posts")
public class PostReportController extends BaseController<ReportService> implements IdDecodable{

	protected PostReportController(ReportService service) {
		super(service);
	}

	@PostMapping("{postId}/reports")
	@Operation(summary = "Report a post", description = "Flag a post for review if it violates rules or contains inappropriate content.")
	public ResponseEntity<ReportResponse> report(@PathVariable String postId, @RequestBody ReportRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(decodeId(postId), ReportTargetType.POST, request));
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.POST;
	}	
}
