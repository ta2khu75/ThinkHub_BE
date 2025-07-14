package com.ta2khu75.thinkhub.result.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta2khu75.thinkhub.result.QuizResultService;
import com.ta2khu75.thinkhub.result.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

@RestController
@RequestMapping("${app.api-prefix}/quiz-results")
public class QuizResultController extends BaseController<QuizResultService> implements IdDecodable {
	public QuizResultController(QuizResultService service) {
		super(service);
	}

//	@PostMapping("{quizId}/start")
//	public ResponseEntity<QuizResultResponse> take(@PathVariable String quizId) {
//		QuizResultResponse response = service.take(decodeIdWithConfig(quizId, IdConfig.QUIZ));
//		return ResponseEntity.ok(response);
//	}

	@PostMapping("{id}/submit")
	public ResponseEntity<QuizResultResponse> submit(@PathVariable String id,
			@RequestBody QuizResultRequest quizResultRequest) {
		return ResponseEntity.ok(service.submit(decodeId(id), quizResultRequest));
	}

	@GetMapping
	public ResponseEntity<PageResponse<QuizResultResponse>> readPage(
			@SnakeCaseModelAttribute QuizResultSearch searchRequest) {
		return ResponseEntity.ok(service.search(searchRequest));
	}

	@GetMapping("{id}")
	public ResponseEntity<QuizResultResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(decodeId(id)));
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ_RESULT;
	}
}
