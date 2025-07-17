package com.ta2khu75.thinkhub.quiz.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.result.QuizResultService;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.controller.BaseController;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Quiz", description = "Create, manage, take, and interact with quizzes including commenting and reporting.")
@ApiController("${app.api-prefix}/quizzes")
public class QuizController extends BaseController<QuizService> implements IdDecodable {
	private final QuizResultService resultService;

	public QuizController(QuizService service, QuizResultService resultService) {
		super(service);
		this.resultService = resultService;
	}

	@GetMapping
	@Operation(summary = "Search quizzes", description = "Browse and filter quizzes by topic, difficulty, or other criteria.")
	public ResponseEntity<PageResponse<QuizResponse>> search(@SnakeCaseModelAttribute QuizSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@PostMapping
	@Operation(summary = "Create a new quiz", description = "Upload a new quiz with optional image to test user knowledge.")
	public ResponseEntity<QuizResponse> create(@Valid @RequestPart QuizRequest quiz,
			@RequestPart(required = true) MultipartFile image) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(quiz, image));
	}

	@GetMapping("{id}")
	@Operation(summary = "Get quiz by ID", description = "Retrieve a quiz’s basic information by its identifier.")
	public ResponseEntity<QuizResponse> read(@PathVariable String id) {
		return ResponseEntity.ok(service.read(decodeId(id)));
	}

	@GetMapping("{id}/detail")
	@Operation(summary = "Get detailed quiz information", description = "Retrieve a quiz along with all questions, answers, and related data.")
	public ResponseEntity<QuizResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(decodeId(id)));
	}

	@PutMapping("{id}")
	@Operation(summary = "Update an existing quiz", description = "Modify quiz content or replace its associated image.")
	public ResponseEntity<QuizResponse> update(@PathVariable String id, @RequestPart("quiz") QuizRequest quiz,
			@RequestPart(name = "image", required = false) MultipartFile image) throws IOException {
		return ResponseEntity.ok(service.update(decodeId(id), quiz, image));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete a quiz", description = "Permanently remove a quiz from the system.")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(decodeId(id));
		return ResponseEntity.noContent().build();
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}

	@PostMapping("{id}/start")
	@Operation(summary = "Start a quiz", description = "Initiate the quiz attempt process for a specific quiz.")
	public ResponseEntity<QuizResultResponse> take(@PathVariable String id) {
		QuizResultResponse response = resultService.take(decodeId(id));
		return ResponseEntity.ok(response);
	}

	@PostMapping("{quizId}/comments")
	@Operation(summary = "Add a comment to a quiz", description = "Leave a comment or feedback on a specific quiz.")
	public ResponseEntity<CommentResponse> comment(@PathVariable String quizId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.comment(decodeId(quizId), request));
	}

	@GetMapping("{quizId}/comments")
	@Operation(summary = "View comments on a quiz", description = "Retrieve all user comments on a specific quiz, with support for pagination.")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComments(@PathVariable String quizId,
			@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readPageComments(decodeId(quizId), search));
	}

	@PostMapping("{quizId}/reports")
	@Operation(summary = "Report a quiz", description = "Flag a quiz for review due to inappropriate content or other concerns.")
	public ResponseEntity<ReportResponse> report(@PathVariable String quizId, @RequestBody ReportRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.report(decodeId(quizId), request));
	}
}
//	@GetMapping("mine/{keyword}")
//	public ResponseEntity<List<QuizResponse>> mySearch(@PathVariable String keyword) {
//		Long authorId = SecurityUtil.getCurrentProfileId();
//		return ResponseEntity.ok(service.readAllByAuthorIdAndKeywork(authorId, keyword));
//	}
//
//	@GetMapping("mine/ids")
//	public ResponseEntity<List<ExamResponse>> myReadAllById(@RequestParam("ids") List<String> ids) {
//		return ResponseEntity.ok(service.myReadAllById(ids));
//	}
//
//	@GetMapping("mine/blog-null")
//	public ResponseEntity<PageResponse<ExamResponse>> readExamBlogNull(@RequestParam("keyword") String keyword,
//			@RequestParam(name = "page", defaultValue = "1", required = false) int page,
//			@RequestParam(name = "size", defaultValue = "5", required = false) int size) {
//		Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
//		return ResponseEntity.ok(service.mySearchExamNull(keyword, pageable));
//	}
//
//	@GetMapping("my-exam/count")
//	public ResponseEntity<CountResponse> countMyExam() {
//		String email = SecurityUtil.getCurrentUserLogin()
//				.orElseThrow(() -> new UnAuthorizedException("You must login first!"));
//		return ResponseEntity.ok(new CountResponse(service.countByAuthorEmail(email)));
//	}
//
//	@GetMapping("{authorId}/count")
//	public ResponseEntity<CountResponse> countExamAuthor(@PathVariable("authorId") String id) {
//		return ResponseEntity
//				.ok(new CountResponse(service.countByAuthorIdAndAccessModifier(id, AccessModifier.PUBLIC)));
//	}
