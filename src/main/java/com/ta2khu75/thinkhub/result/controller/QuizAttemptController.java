package com.ta2khu75.thinkhub.result.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.result.QuizResultService;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "QuizAttempt", description = "Manage quiz attempt lifecycle")
@ApiController("${app.api-prefix}/quiz")
public class QuizAttemptController implements IdDecodable {

    private final QuizResultService quizResultService;

    public QuizAttemptController(QuizResultService quizResultService) {
        this.quizResultService = quizResultService;
    }

    @PostMapping("{id}/start")
    @Operation(summary = "Start a quiz")
    public ResponseEntity<QuizResultResponse> start(@PathVariable String id) {
        return ResponseEntity.ok(quizResultService.take(decodeId(id)));
    }

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}
}
