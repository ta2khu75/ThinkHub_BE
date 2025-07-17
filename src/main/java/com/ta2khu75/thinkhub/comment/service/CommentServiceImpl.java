package com.ta2khu75.thinkhub.comment.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.comment.CommentTargetType;
import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.entity.Comment;
import com.ta2khu75.thinkhub.comment.mapper.CommentMapper;
import com.ta2khu75.thinkhub.comment.repository.CommentRepository;
import com.ta2khu75.thinkhub.report.ReportService;
import com.ta2khu75.thinkhub.report.ReportTargetType;
import com.ta2khu75.thinkhub.report.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.dto.ReportResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
public class CommentServiceImpl extends BaseService<Comment, Long, CommentRepository, CommentMapper>
		implements CommentService {
	private final AccountService accountService;
	private final ReportService reportService;

	public CommentServiceImpl(CommentRepository repository, CommentMapper mapper, AccountService accountService,
			ReportService reportService) {
		super(repository, mapper);
		this.accountService = accountService;
		this.reportService = reportService;
	}

	@Override
	public PageResponse<CommentResponse> readPageBy(Long targetId, CommentTargetType targetType, Search search) {
		Page<Comment> page = repository.findByTargetIdAndTargetType(targetId, targetType, search.toPageable());
		Set<Long> authorIds = page.getContent().stream().map(Comment::getAuthorId).collect(Collectors.toSet());
		Map<Long, AuthorResponse> authorMap = accountService.readMapAuthorsByAccountIds(authorIds);
		List<CommentResponse> comments = page.getContent().stream().map(comment -> {
			CommentResponse response = mapper.convert(comment);
			response.setAuthor(authorMap.get(comment.getAuthorId()));
			return response;
		}).toList();
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), comments);
	}

	@Override
	public CommentResponse create(Long targetId, CommentTargetType targetType, CommentRequest request) {
		AuthorResponse author = accountService.readAuthor(SecurityUtil.getCurrentAccountIdDecode());
		Comment comment = mapper.toEntity(request);
		comment.setAuthorId(SecurityUtil.getCurrentAccountIdDecode());
		comment.setTargetId(targetId);
		comment.setTargetType(targetType);
		CommentResponse response = mapper.convert(repository.save(comment));
		response.setAuthor(author);
		return response;
	}

	@Override
	public CommentResponse update(Long id, CommentRequest request) {
		Comment comment = this.readEntity(id);
		if (comment.getAuthorId().equals(SecurityUtil.getCurrentAccountIdDecode())) {
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
	public ReportResponse report(Long id, ReportRequest request) {
		return reportService.create(id, ReportTargetType.COMMENT, request);
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
