package com.ta2khu75.thinkhub.quiz.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.shared.controller.BaseController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("${app.api-prefix}/quizzes")
public class QuizController extends BaseController<QuizService> {
	public QuizController(QuizService service, ObjectMapper objectMapper) {
		super(service);
	}

	public ResponseEntity<QuizResponse> create(@Valid @RequestPart QuizRequest quiz,
			@RequestPart(required = true) MultipartFile image) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(quiz, image));
	}

	@GetMapping("{id}")
	@EndpointMapping(name = "Read quiz")
	public ResponseEntity<QuizResponse> read(@PathVariable String id) {
		return ResponseEntity.ok(service.read(id));
	}

	@GetMapping("{id}/detail")
	@EndpointMapping(name = "Read quiz detail")
	public ResponseEntity<QuizResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(id));
	}

	@EndpointMapping(name = "Update quiz")
	@PutMapping(path = "{id}", consumes = "multipart/form-data")
	@PreAuthorize("@ownerSecurity.isExamOwner(#id)")
	public ResponseEntity<QuizResponse> update(@PathVariable String id, @RequestPart("quiz") String quizString,
			@RequestPart(name = "image", required = false) MultipartFile image) throws IOException {
		QuizRequest quiz = objectMapper.readValue(quizString, QuizRequest.class);
		return ResponseEntity.ok(service.update(id, quiz, image));
	}

	@DeleteMapping("{id}")
	@EndpointMapping(name = "Delete quiz")
	@PreAuthorize("@ownerSecurity.isExamOwner(#id) or hasRole('ROOT')")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	@EndpointMapping(name = "Search quiz")
	public ResponseEntity<PageResponse<QuizResponse>> search(QuizSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@GetMapping("mine/{keyword}")
	@EndpointMapping(name = "Search my quiz by keyword")
	public ResponseEntity<List<QuizResponse>> mySearch(@PathVariable String keyword) {
		Long authorId = SecurityUtil.getCurrentProfileId();
		return ResponseEntity.ok(service.readAllByAuthorIdAndKeywork(authorId, keyword));
	}

//	@GetMapping("mine/ids")
//	public ResponseEntity<List<ExamResponse>> myReadAllById(@RequestParam("ids") List<String> ids) {
//		return ResponseEntity.ok(service.myReadAllById(ids));
//	}
//	@GetMapping("mine/blog-null")
//	public ResponseEntity<PageResponse<ExamResponse>> readExamBlogNull(@RequestParam("keyword") String keyword,
//			@RequestParam(name = "page", defaultValue = "1", required = false) int page,
//			@RequestParam(name = "size", defaultValue = "5", required = false) int size) {
//		Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
//		return ResponseEntity.ok(service.mySearchExamNull(keyword, pageable));
//	}

//	@GetMapping("my-exam/count")
//	public ResponseEntity<CountResponse> countMyExam() {
//		String email = SecurityUtil.getCurrentUserLogin()
//				.orElseThrow(() -> new UnAuthorizedException("You must login first!"));
//		return ResponseEntity.ok(new CountResponse(service.countByAuthorEmail(email)));
//	}

//	@GetMapping("{authorId}/count")
//	public ResponseEntity<CountResponse> countExamAuthor(@PathVariable("authorId") String id) {
//		return ResponseEntity
//				.ok(new CountResponse(service.countByAuthorIdAndAccessModifier(id, AccessModifier.PUBLIC)));
//	}
}
