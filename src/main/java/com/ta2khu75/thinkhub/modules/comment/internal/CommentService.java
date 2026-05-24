package com.ta2khu75.thinkhub.modules.comment.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.comment.api.CommentApi;
import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentRequest;
import com.ta2khu75.thinkhub.modules.comment.api.dto.CommentResponse;
import com.ta2khu75.thinkhub.modules.comment.api.model.CommentTargetType;
import com.ta2khu75.thinkhub.modules.comment.internal.CommentService;
import com.ta2khu75.thinkhub.modules.comment.internal.domain.Comment;
import com.ta2khu75.thinkhub.modules.comment.internal.mapper.CommentMapper;
import com.ta2khu75.thinkhub.modules.comment.internal.repository.CommentRepository;
import com.ta2khu75.thinkhub.modules.comment.internal.validator.CommentValidator;
import com.ta2khu75.thinkhub.modules.comment.required.port.CommentUserPort;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfigRegistry;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
class CommentService extends BaseService<Comment, Long, CommentRepository> implements CommentApi, IdDecodable {

	public CommentService(CommentRepository repository, CommentValidator validator, CommentUserPort userPort,
			CommentMapper mapper) {
		super(repository);
		this.validator = validator;
		this.userPort = userPort;
		this.mapper = mapper;
	}

	private final CommentValidator validator;
	private final CommentUserPort userPort;
	private final CommentMapper mapper;

	@Override
	public PageResponse<CommentResponse> readPageBy(String targetId, CommentTargetType targetType, Search search) {
		Long id = getTargetId(targetId, targetType);
		Page<Comment> page = repository.findByTargetIdAndTargetType(id, targetType, search.toPageable());
		Set<Long> authorIds = page.getContent().stream().map(Comment::getAuthorId).collect(Collectors.toSet());
		Map<Long, AuthorResponse> authorMap = userPort.readMapAuthorsByUserIds(authorIds);
		List<CommentResponse> comments = page.getContent().stream().map(comment -> {
			CommentResponse response = mapper.convert(comment);
			response.setAuthor(authorMap.get(comment.getAuthorId()));
			return response;
		}).toList();
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), comments);
	}

	@Override
	public CommentResponse create(String targetId, CommentTargetType targetType, CommentRequest request) {
		AuthorResponse author = userPort.readAuthor(SecurityUtil.getCurrentUserId());
		Comment comment = mapper.toEntity(request);
		comment.setAuthorId(SecurityUtil.getCurrentUserIdDecode());
		comment.setTargetId(getTargetId(targetId, targetType));
		comment.setTargetType(targetType);
		CommentResponse response = mapper.convert(repository.save(comment));
		response.setAuthor(author);
		return response;
	}

	private Long getTargetId(String targetId, CommentTargetType targetType) {
		switch (targetType) {
		case POST: {
			return decodeId(targetId, IdConfigRegistry.resolve(IdSubject.POST));
		}
		case QUIZ: {
			return decodeId(targetId, IdConfigRegistry.resolve(IdSubject.QUIZ));
		}
		case COMMENT: {
			return Long.valueOf(targetId);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + targetType);
		}
	}

	@Override
	public CommentResponse update(Long id, CommentRequest request) {
		Comment comment = this.readEntity(id);
		Long currentUserId = SecurityUtil.getCurrentUserIdDecode();
		validator.validateUpdate(comment, currentUserId);
		comment.setContent(request.getContent());
		AuthorResponse author = userPort.readAuthor(SecurityUtil.getCurrentUserId());
		CommentResponse response = mapper.convert(repository.save(comment));
		response.setAuthor(author);
		return response;
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public CommentResponse read(Long id) {
		Comment comment = this.readEntity(id);
		AuthorResponse author = userPort.readAuthor(comment.getAuthorId());
		CommentResponse response = mapper.convert(comment);
		response.setAuthor(author);
		return response;
	}

	@Override
	public IdConfig getIdConfig() {
		// TODO Auto-generated method stub
		return null;
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
