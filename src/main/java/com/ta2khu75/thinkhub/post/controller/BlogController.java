package com.ta2khu75.thinkhub.post.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ta2khu75.quiz.anotation.EndpointMapping;
import com.ta2khu75.quiz.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.quiz.model.request.BlogRequest;
import com.ta2khu75.quiz.model.request.CommentRequest;
import com.ta2khu75.quiz.model.request.search.BlogSearch;
import com.ta2khu75.quiz.model.request.search.Search;
import com.ta2khu75.quiz.model.response.BlogResponse;
import com.ta2khu75.quiz.model.response.CommentResponse;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.service.BlogService;
import com.ta2khu75.quiz.service.CommentService;
import com.ta2khu75.quiz.util.SecurityUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${app.api-prefix}/blogs")
public class BlogController extends BaseController<BlogService> {

	private final ObjectMapper objectMapper;
	private final CommentService commentService;

	public BlogController(BlogService service, ObjectMapper objectMapper, CommentService commentService) {
		super(service);
		this.objectMapper = objectMapper;
		this.commentService = commentService;
	}

	@GetMapping
	@EndpointMapping(name = "Search blog")
	public ResponseEntity<PageResponse<BlogResponse>> search(@SnakeCaseModelAttribute BlogSearch blogSearch) {
		return ResponseEntity.ok(service.search(blogSearch));
	}

	@GetMapping("mine/{keywork}")
	@EndpointMapping(name = "Search my blog by keyword")
	public ResponseEntity<List<BlogResponse>> searchMyBlog(@PathVariable String keywork) {
		Long authorId = SecurityUtil.getCurrentProfileId();

		return ResponseEntity.ok(service.readAllByAuthorIdAndKeywork(authorId, keywork));
	}

	@GetMapping("{id}/comments")
	@EndpointMapping(name = "Read blog comments")
	public ResponseEntity<PageResponse<CommentResponse>> readPageComment(@PathVariable String id,
			@ModelAttribute Search search) {
		return ResponseEntity.ok(commentService.readPageByBlogId(id, search));
	}

	@PostMapping("{id}/comments")
	@EndpointMapping(name = "Create blog comment")
	public ResponseEntity<CommentResponse> createComment(@PathVariable String id,
			@Valid @RequestBody CommentRequest request) {
		return ResponseEntity.ok(commentService.create(id, request));
	}

	@GetMapping("/{id}")
	@EndpointMapping(name = "Read blog")
	public ResponseEntity<BlogResponse> read(@PathVariable String id) {
		return ResponseEntity.ok(service.read(id));
	}

	@GetMapping("/{id}/detail")
	@EndpointMapping(name = "Read blog detail")
	public ResponseEntity<BlogResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(id));
	}

	@PostMapping(consumes = "multipart/form-data")
	@EndpointMapping(name = "Create blog")
	public ResponseEntity<BlogResponse> create(@RequestPart("blog") String request,
			@RequestPart(name = "image", required = false) MultipartFile file) throws IOException {
		BlogRequest blogRequest = objectMapper.readValue(request, BlogRequest.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(blogRequest, file));
	}

	@PreAuthorize("@ownerSecurity.isBlogOwner(#id)")
	@PutMapping(path = "/{id}", consumes = "multipart/form-data")
	@EndpointMapping(name = "Update blog")
	public ResponseEntity<BlogResponse> update(@PathVariable String id, @RequestPart("blog") String request,
			@RequestPart(name = "image", required = false) MultipartFile file) throws IOException {
		BlogRequest blogRequest = objectMapper.readValue(request, BlogRequest.class);
		return ResponseEntity.ok(service.update(id, blogRequest, file));
	}

	@DeleteMapping("/{id}")
	@EndpointMapping(name = "Delete blog")
	@PreAuthorize("@ownerSecurity.isBlogOwner(#id) or hasRole('ROOT')")
	public ResponseEntity<BlogResponse> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

//	@GetMapping("my-blog/count")
//	public ResponseEntity<CountResponse> countMyBlog() {
//		String email = SecurityUtil.getCurrentUserLogin()
//				.orElseThrow(() -> new UnAuthorizedException("You must login first!"));
//		return ResponseEntity.ok(new CountResponse(service.countByAuthorEmail(email)));
//	}

//	@GetMapping("{authorId}/count")
//	public ResponseEntity<CountResponse> countBlogAuthor(@PathVariable("authorId") String id) {
//		return ResponseEntity
//				.ok(new CountResponse(service.countByAuthorIdAndAccessModifier(id, AccessModifier.PUBLIC)));
//	}
}
