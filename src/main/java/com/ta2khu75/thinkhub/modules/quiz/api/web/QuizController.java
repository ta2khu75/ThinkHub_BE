package com.ta2khu75.thinkhub.modules.quiz.api.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ta2khu75.thinkhub.modules.quiz.api.QuizApi;
import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizRequest;
import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizResponse;
import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.shared.common.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.common.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.common.api.controller.CrudController;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Quiz", description = "Create, manage, take, and interact with quizzes including commenting and reporting.")
@ApiController("${app.api-prefix}/quizzes")
class QuizController extends BaseController<QuizApi> implements CrudController<QuizRequest, QuizResponse, String> {

	protected QuizController(QuizApi api) {
		super(api);
	}

	@GetMapping
	@Operation(summary = "Search quizzes", description = "Browse and filter quizzes by topic, difficulty, or other criteria.")
	public ResponseEntity<PageResponse<QuizResponse>> search(@SnakeCaseModelAttribute QuizSearch search) {
		return ResponseEntity.ok(api.search(search));
	}

	@Override
	@Operation(summary = "Create a new quiz", description = "Upload a new quiz with optional image to test user knowledge.")
	public ResponseEntity<QuizResponse> create(@Valid QuizRequest quiz) {
		return ResponseEntity.status(HttpStatus.CREATED).body(api.create(quiz));
	}

	@Override
	@Operation(summary = "Update an existing quiz", description = "Modify quiz content or replace its associated image.")
	public ResponseEntity<QuizResponse> update(String id, QuizRequest quiz) {
		return ResponseEntity.ok(api.update(id, quiz));
	}

	@Override
	@Operation(summary = "Get quiz by ID", description = "Retrieve a quiz’s basic information by its identifier.")
	public ResponseEntity<QuizResponse> read(String id) {
		return ResponseEntity.ok(api.read(id));
	}

	@GetMapping("{id}/detail")
	@Operation(summary = "Get detailed quiz information", description = "Retrieve a quiz along with all questions, answers, and related data.")
	public ResponseEntity<QuizResponse> readDetail(String id) {
		return ResponseEntity.ok(api.readDetail(id));
	}

	@PostMapping("{id}/disable")
	@Operation(summary = "Disable a quiz", description = "Temporarily remove a quiz from the system.")
	public ResponseEntity<Void> disable(String id) {
		api.disable(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("{id}/publish")
	@Operation(summary = "Publish a quiz", description = "Publish a quiz to share with others.")
	public ResponseEntity<Void> publish(String id) {
		api.publish(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("{id}/hide")
	@Operation(summary = "Hide a quiz", description = "Hide a quiz from public view.")
	public ResponseEntity<Void> hide(String id) {
		api.hide(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Delete a quiz", description = "Permanently remove a quiz from the system.")
	public ResponseEntity<Void> delete(String id) {
		api.delete(id);
		return ResponseEntity.noContent().build();
	}

}
//	@GetMapping("mine/{keyword}")
//	public ResponseEntity<List<QuizResponse>> mySearch(@PathVariable String keyword) {
//		Long authorId = SecurityUtil.getCurrentProfileId();
//		return ResponseEntity.ok(api.readAllByAuthorIdAndKeywork(authorId, keyword));
//	}
//
//	@GetMapping("mine/ids")
//	public ResponseEntity<List<ExamResponse>> myReadAllById(@RequestParam("ids") List<String> ids) {
//		return ResponseEntity.ok(api.myReadAllById(ids));
//	}
//
//	@GetMapping("mine/blog-null")
//	public ResponseEntity<PageResponse<ExamResponse>> readExamBlogNull(@RequestParam("keyword") String keyword,
//			@RequestParam(name = "page", defaultValue = "1", required = false) int page,
//			@RequestParam(name = "size", defaultValue = "5", required = false) int size) {
//		Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
//		return ResponseEntity.ok(api.mySearchExamNull(keyword, pageable));
//	}
//
//	@GetMapping("my-exam/count")
//	public ResponseEntity<CountResponse> countMyExam() {
//		String email = SecurityUtil.getCurrentUserLogin()
//				.orElseThrow(() -> new UnAuthorizedException("You must login first!"));
//		return ResponseEntity.ok(new CountResponse(api.countByAuthorEmail(email)));
//	}
//
//	@GetMapping("{authorId}/count")
//	public ResponseEntity<CountResponse> countExamAuthor(@PathVariable("authorId") String id) {
//		return ResponseEntity
//				.ok(new CountResponse(api.countByAuthorIdAndAccessModifier(id, AccessModifier.PUBLIC)));
//	}
