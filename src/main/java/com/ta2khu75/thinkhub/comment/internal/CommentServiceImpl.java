package com.ta2khu75.thinkhub.comment.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.comment.api.CommentApi;
import com.ta2khu75.thinkhub.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.internal.entity.Comment;
import com.ta2khu75.thinkhub.comment.internal.entity.CommentTargetType;
import com.ta2khu75.thinkhub.comment.internal.mapper.CommentMapper;
import com.ta2khu75.thinkhub.comment.internal.repository.CommentRepository;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.UserApi;

@Service
public class CommentServiceImpl extends BaseService<Comment, Long, CommentRepository, CommentMapper>
		implements CommentApi {
	private final UserApi accountService;

	public CommentServiceImpl(CommentRepository repository, CommentMapper mapper, UserApi accountService) {
		super(repository, mapper);
		this.accountService = accountService;
	}

	@Override
	public PageResponse<CommentResponse> readPageBy(Long targetId, CommentTargetType targetType, Search search) {
		Page<Comment> page = repository.findByTargetIdAndTargetType(targetId, targetType, search.toPageable());
		Set<Long> authorIds = page.getContent().stream().map(Comment::getAuthorId).collect(Collectors.toSet());
		Map<Long, AuthorResponse> authorMap = accountService.readMapAuthorsByUserIds(authorIds);
		List<CommentResponse> comments = page.getContent().stream().map(comment -> {
			CommentResponse response = mapper.convert(comment);
			response.setAuthor(authorMap.get(comment.getAuthorId()));
			return response;
		}).toList();
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), comments);
	}

	@Override
	public CommentResponse create(Long targetId, CommentTargetType targetType, CommentRequest request) {
		AuthorResponse author = accountService.readAuthor(SecurityUtil.getCurrentUserIdDecode());
		Comment comment = mapper.toEntity(request);
		comment.setAuthorId(SecurityUtil.getCurrentUserIdDecode());
		comment.setTargetId(targetId);
		comment.setTargetType(targetType);
		CommentResponse response = mapper.convert(repository.save(comment));
		response.setAuthor(author);
		return response;
	}

	@Override
	public CommentResponse update(Long id, CommentRequest request) {
		Comment comment = this.readEntity(id);
		if (comment.getAuthorId().equals(SecurityUtil.getCurrentUserIdDecode())) {
			comment.setContent(request.getContent());
			AuthorResponse author = accountService.readAuthor(comment.getAuthorId());
			CommentResponse response = mapper.convert(repository.save(comment));
			response.setAuthor(author);
			return response;
		}
		throw new InvalidDataException("You are not allowed to update this comment");
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public CommentResponse read(Long id) {
		Comment comment = this.readEntity(id);
		AuthorResponse author = accountService.readAuthor(comment.getAuthorId());
		CommentResponse response = mapper.convert(comment);
		response.setAuthor(author);
		return response;
	}

//	@Override
//	@Transactional
//	public CommentResponse create(Long id, CommentTargetType   , CommentRequest request) {
//		Comment comment = mapper.toEntity(request);
//		comment.setAuthor(SecurityUtil.getCurrentProfile());
//		comment.setBlog(Blog.fromEncodedId(blogId));
//		return mapper.toResponse(repository.save(comment));
//	}
//
//	@Override
//	public CommentResponse update(String id, CommentRequest request) {
//		Comment comment = FunctionUtil.findOrThrow(id, Comment.class, repository::findById);
//		comment.setContent(request.getContent());
//		return mapper.toResponse(repository.save(comment));
//	}
//
//	@Override
//	public void delete(String id) {
//		repository.deleteById(id);
//	}
//
//	@Override
//	public PageResponse<CommentResponse> readPageByBlogId(String blogId, Search search) {
//		Page<Comment> commentPage = repository.findByBlogId(blogId, search.toPageable());
//		return mapper.toPageResponse(commentPage);
//	}
//
//	@Override
//	public PageResponse<CommentResponse> readPageByBlogId(Long blogId, Search search) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommentResponse create(Long blogId, CommentRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommentResponse update(Long id, CommentRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
