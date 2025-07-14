package com.ta2khu75.thinkhub.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.quiz.mapper.CommentMapper;
import com.ta2khu75.quiz.model.entity.Blog;
import com.ta2khu75.quiz.model.entity.Comment;
import com.ta2khu75.quiz.model.request.CommentRequest;
import com.ta2khu75.quiz.model.request.search.Search;
import com.ta2khu75.quiz.model.response.CommentResponse;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.repository.BlogRepository;
import com.ta2khu75.quiz.repository.CommentRepository;
import com.ta2khu75.quiz.service.CommentService;
import com.ta2khu75.quiz.service.base.BaseService;
import com.ta2khu75.quiz.util.FunctionUtil;
import com.ta2khu75.quiz.util.SecurityUtil;

@Service
public class CommentServiceImpl extends BaseService<CommentRepository, CommentMapper> implements CommentService {

	public CommentServiceImpl(CommentRepository repository, CommentMapper mapper, BlogRepository blogRepository) {
		super(repository, mapper);
	}

	@Override
	@Transactional
	public CommentResponse create(String blogId ,CommentRequest request) {
		Comment comment = mapper.toEntity(request);
		comment.setAuthor(SecurityUtil.getCurrentProfile());
		comment.setBlog(Blog.fromEncodedId(blogId));
		return mapper.toResponse(repository.save(comment));
	}

	@Override
	public CommentResponse update(String id, CommentRequest request) {
		Comment comment = FunctionUtil.findOrThrow(id, Comment.class, repository::findById);
		comment.setContent(request.getContent());
		return mapper.toResponse(repository.save(comment));
	}


	@Override
	public void delete(String id) {
		repository.deleteById(id);
	}

	@Override
	public PageResponse<CommentResponse> readPageByBlogId(String blogId, Search search) {
		Page<Comment> commentPage= repository.findByBlogId(blogId, search.toPageable());
		return mapper.toPageResponse(commentPage);
	}

}
