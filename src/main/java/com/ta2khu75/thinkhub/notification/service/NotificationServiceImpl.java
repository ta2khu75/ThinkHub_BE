package com.ta2khu75.thinkhub.notification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.comment.CommentService;
import com.ta2khu75.thinkhub.notification.NotificationService;
import com.ta2khu75.thinkhub.notification.dto.NotificationIdDto;
import com.ta2khu75.thinkhub.notification.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.dto.NotificationStatusRequest;
import com.ta2khu75.thinkhub.notification.entity.Notification;
import com.ta2khu75.thinkhub.notification.entity.NotificationId;
import com.ta2khu75.thinkhub.notification.mapper.NotificationMapper;
import com.ta2khu75.thinkhub.notification.repository.NotificationRepository;
import com.ta2khu75.thinkhub.post.PostService;
import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
public class NotificationServiceImpl
		extends BaseService<Notification, NotificationId, NotificationRepository, NotificationMapper>
		implements NotificationService {
	private final PostService postService;
	private final QuizService quizService;
	private final CommentService commentService;

	public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper,
			PostService postService, QuizService quizService, CommentService commentService) {
		super(repository, mapper);
		this.postService = postService;
		this.quizService = quizService;
		this.commentService = commentService;
	}

	@Override
	public NotificationResponse create(NotificationRequest request) {
		Notification notification = mapper.toEntity(request);
		notification = repository.save(notification);
		NotificationResponse response = mapper.convert(notification);
		response.setId(toNotificationIdDto(notification.getId()));
		return response;

	}

	@Override
	public NotificationResponse update(NotificationStatusRequest request) {
		NotificationId notificationId = toNotificationId(request.id());
		Notification notification = readEntity(notificationId);
		notification.setStatus(request.status());
		repository.save(notification);
		NotificationResponse response = mapper.convert(notification);
		response.setId(toNotificationIdDto(notification.getId()));
		return response;
	}

	@Override
	public void delete(NotificationIdDto id) {
		repository.deleteById(toNotificationId(id));

	}

	@Override
	public PageResponse<NotificationResponse> readPage(Search search) {
		Long accountId = SecurityUtil.getCurrentAccountIdDecode();
		Page<Notification> page = repository.findByIdAccountId(accountId, search.toPageable());
		List<NotificationResponse> notifications = page.getContent().stream().map(notification -> {
			NotificationResponse response = mapper.convert(notification);
			response.setTarget(this.resolveTarget(notification));
			response.setId(toNotificationIdDto(notification.getId()));
			return response;
		}).toList();
		return new PageResponse<>(page.getNumber(), page.getTotalElements(), page.getTotalPages(), notifications);
	}

	private Object resolveTarget(Notification notification) {
		Long targetId = notification.getId().getTargetId();
		switch (notification.getId().getTargetType()) {
		case POST:
			return postService.read(targetId);
		case QUIZ:
			return quizService.read(targetId);
		case COMMENT:
			return commentService.read(targetId);
		default:
			throw new IllegalArgumentException("Unexpected value: " + notification.getId().getTargetType());
		}
	}

	private NotificationIdDto toNotificationIdDto(NotificationId id) {
		String accountId = IdConverterUtil.encode(id.getAccountId(), IdConfig.ACCOUNT);
		switch (id.getTargetType()) {
		case POST:
			String postId = IdConverterUtil.encode(id.getTargetId(), IdConfig.POST);
			return new NotificationIdDto(accountId, postId, id.getTargetType());
		case QUIZ:
			String quizId = IdConverterUtil.encode(id.getTargetId(), IdConfig.QUIZ);
			return new NotificationIdDto(accountId, quizId, id.getTargetType());
		case COMMENT:
			return new NotificationIdDto(accountId, id.getTargetId().toString(), id.getTargetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + id.getTargetType());
		}
	}

	private NotificationId toNotificationId(NotificationIdDto id) {
		Long accountId = IdConverterUtil.decode(id.accountId(), IdConfig.ACCOUNT);
		switch (id.targetType()) {
		case POST:
			Long postId = IdConverterUtil.decode(id.targetId(), IdConfig.POST);
			return new NotificationId(accountId, postId, id.targetType());
		case QUIZ:
			Long quizId = IdConverterUtil.decode(id.targetId(), IdConfig.QUIZ);
			return new NotificationId(accountId, quizId, id.targetType());
		case COMMENT:
			return new NotificationId(accountId, Long.valueOf(id.targetId()), id.targetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + id.targetType());
		}
	}
//	private final BlogService blogService;
//	private final QuizService quizService;
//
//	public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper,
//			BlogService blogService, QuizService quizService) {
//		super(repository, mapper);
//		this.blogService = blogService;
//		this.quizService = quizService;
//	}
//
////	@Override
////	public Notification create(@Valid Notification request) {
////		return repository.save(request);
////	}
////
////	@Override
////	public Notification update(NotificationId id, @Valid Notification request) {
////		return null;
////	}
////
////	@Override
////	public Notification read(NotificationId id) {
////		return null;
////	}
////
////	@Override
////	public void delete(NotificationId id) {
////		repository.deleteById(id);
////	}
//
//	@Override
//	public PageResponse<NotificationResponse> readPageByAccountId(String accountId, Pageable pageable) {
//		Page<Notification> page = repository.findByAccountId(accountId, pageable);
//		PageResponse<NotificationResponse> response = mapper.toPageResponse(page);
//		response.getContent().forEach(notification -> notification.setTarget(this.resolveTarget(notification)));
//		return response;
//	}
//
//	private Object resolveTarget(NotificationResponse notification) {
//		switch (notification.getId().getTargetType()) {
//		case BLOG:
//			return blogService.read(Base62.encodeWithSalt(notification.getId().getTargetId(), SaltedType.BLOG));
//		case QUIZ:
//			return quizService.read(Base62.encodeWithSalt(notification.getId().getTargetId(), SaltedType.QUIZ));
//		default:
//			throw new IllegalArgumentException("Unsupported target type: " + notification.getId().getTargetType());
//		}
//	}
}
