package com.ta2khu75.thinkhub.post.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ta2khu75.thinkhub.post.api.dto.PostRequest;
import com.ta2khu75.thinkhub.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.controller.CrudController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Post", description = "Create, manage, and interact with posts including commenting and reporting.")
@ApiController("${app.api-prefix}/posts")
public class PostController extends BaseController<PostApi>
		implements CrudController<PostRequest, PostResponse, String>, IdDecodable {
	protected PostController(PostApi service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "Search posts", description = "Look up posts using filters, sorting, and pagination to explore content.")
	public ResponseEntity<PageResponse<PostResponse>> search(PostSearch search) {
		return ResponseEntity.ok(service.search(search));
	}

	@Override
	@Operation(summary = "Create a new post", description = "Publish a new post to share content with others.")
	public ResponseEntity<PostResponse> create(@Valid PostRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	@Override
	@Operation(summary = "Update a post", description = "Edit the content or metadata of an existing post.")
	public ResponseEntity<PostResponse> update(String id, @Valid PostRequest request) {
		return ResponseEntity.ok(service.update(decodeId(id), request));
	}

	@Override
	@Operation(summary = "Delete a post", description = "Permanently remove a post from the system.")
	public ResponseEntity<Void> delete(String id) {
		service.delete(decodeId(id));
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Get a post", description = "Retrieve detailed information about a specific post.")
	public ResponseEntity<PostResponse> read(String id) {
		return ResponseEntity.ok(service.read(decodeId(id)));
	}
	@GetMapping("{id}/detail")
	@Operation(summary = "Get a post detail", description = "Returns the detailed information of a specific post.")
	public ResponseEntity<PostResponse> readDetail(@PathVariable String id) {
		return ResponseEntity.ok(service.readDetail(decodeId(id)));
	}


	@Override
	public IdConfig getIdConfig() {
		return IdConfig.POST;
	}
}
//	private final ObjectMapper objectMapper;
//	private final CommentService commentService;
//
//	public BlogController(BlogService service, ObjectMapper objectMapper, CommentService commentService) {
//		super(service);
//		this.objectMapper = objectMapper;
//		this.commentService = commentService;
//	}
//
//	@GetMapping
//	@EndpointMapping(name = "Search blog")
//	public ResponseEntity<PageResponse<BlogResponse>> search(@SnakeCaseModelAttribute BlogSearch blogSearch) {
//		return ResponseEntity.ok(service.search(blogSearch));
//	}
//
//	@GetMapping("mine/{keywork}")
//	@EndpointMapping(name = "Search my blog by keyword")
//	public ResponseEntity<List<BlogResponse>> searchMyBlog(@PathVariable String keywork) {
//		Long authorId = SecurityUtil.getCurrentProfileId();
//
//		return ResponseEntity.ok(service.readAllByAuthorIdAndKeywork(authorId, keywork));
//	}
//
//	@GetMapping("{id}/comments")
//	@EndpointMapping(name = "Read blog comments")
//	public ResponseEntity<PageResponse<CommentResponse>> readPageComment(@PathVariable String id,
//			@ModelAttribute Search search) {
//		return ResponseEntity.ok(commentService.readPageByBlogId(id, search));
//	}
//
//	@PostMapping("{id}/comments")
//	@EndpointMapping(name = "Create blog comment")
//	public ResponseEntity<CommentResponse> createComment(@PathVariable String id,
//			@Valid @RequestBody CommentRequest request) {
//		return ResponseEntity.ok(commentService.create(id, request));
//	}
//
//	@GetMapping("/{id}")
//	@EndpointMapping(name = "Read blog")
//	public ResponseEntity<BlogResponse> read(@PathVariable String id) {
//		return ResponseEntity.ok(service.read(id));
//	}
//
//	@GetMapping("/{id}/detail")
//	@EndpointMapping(name = "Read blog detail")
//	public ResponseEntity<BlogResponse> readDetail(@PathVariable String id) {
//		return ResponseEntity.ok(service.readDetail(id));
//	}
//
//	@PostMapping(consumes = "multipart/form-data")
//	@EndpointMapping(name = "Create blog")
//	public ResponseEntity<BlogResponse> create(@RequestPart("blog") String request,
//			@RequestPart(name = "image", required = false) MultipartFile file) throws IOException {
//		BlogRequest blogRequest = objectMapper.readValue(request, BlogRequest.class);
//		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(blogRequest, file));
//	}
//
//	@PreAuthorize("@ownerSecurity.isBlogOwner(#id)")
//	@PutMapping(path = "/{id}", consumes = "multipart/form-data")
//	@EndpointMapping(name = "Update blog")
//	public ResponseEntity<BlogResponse> update(@PathVariable String id, @RequestPart("blog") String request,
//			@RequestPart(name = "image", required = false) MultipartFile file) throws IOException {
//		BlogRequest blogRequest = objectMapper.readValue(request, BlogRequest.class);
//		return ResponseEntity.ok(service.update(id, blogRequest, file));
//	}
//
//	@DeleteMapping("/{id}")
//	@EndpointMapping(name = "Delete blog")
//	@PreAuthorize("@ownerSecurity.isBlogOwner(#id) or hasRole('ROOT')")
//	public ResponseEntity<BlogResponse> delete(@PathVariable String id) {
//		service.delete(id);
//		return ResponseEntity.noContent().build();
//	}

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
