package com.ta2khu75.thinkhub.result.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.result.QuizResultService;
import com.ta2khu75.thinkhub.result.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "QuizResult", description = "Manage and review quiz submission results from users.")
@ApiController("${app.api-prefix}/quiz-results")
public class QuizResultController extends BaseController<QuizResultService> implements IdDecodable {
	public QuizResultController(QuizResultService service) {
		super(service);
	}

	@PostMapping("{id}/submit")
	@Operation(summary = "Submit quiz result", description = "Submit user answers for a specific quiz and receive the result including score, feedback, and review data.")
	public ResponseEntity<QuizResultResponse> submit(@PathVariable String id,
			@RequestBody QuizResultRequest quizResultRequest) {
		return ResponseEntity.ok(service.submit(decodeId(id), quizResultRequest));
	}

	@GetMapping
	@Operation(summary = "Search quiz results", description = "Search through quiz submission results with filters like user ID, quiz ID, date, and pagination.")
	public ResponseEntity<PageResponse<QuizResultResponse>> readPage(
			@SnakeCaseModelAttribute QuizResultSearch searchRequest) {
		return ResponseEntity.ok(service.search(searchRequest));
	}

	@GetMapping("{id}")
	@Operation(summary = "Read quiz result detail", description = "Fetch detailed result information for a specific quiz submission, including answers and scoring breakdown.")
	public ResponseEntity<QuizResultResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(decodeId(id)));
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ_RESULT;
	}
}
