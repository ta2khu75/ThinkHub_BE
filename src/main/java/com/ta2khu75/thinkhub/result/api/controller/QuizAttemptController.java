package com.ta2khu75.thinkhub.result.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ta2khu75.thinkhub.result.api.QuizResultApi;
import com.ta2khu75.thinkhub.result.api.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "QuizAttempt", description = "Manage quiz attempt lifecycle")
@ApiController("${app.api-prefix}/quiz")
public class QuizAttemptController implements IdDecodable {

    private final QuizResultApi quizResultService;

    public QuizAttemptController(QuizResultApi quizResultService) {
        this.quizResultService = quizResultService;
    }

    @PostMapping("{id}/start")
    @Operation(summary = "Start a quiz")
    public ResponseEntity<QuizResultResponse> start(@PathVariable String id) {
    	QuizResultResponse response = quizResultService.readByQuizId(decodeId(id));
    	if (response != null) {
			return ResponseEntity.ok(response);
		}
        return ResponseEntity.status(HttpStatus.CREATED).body(quizResultService.take(decodeId(id)));
    }

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}
}
