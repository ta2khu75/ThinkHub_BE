package com.ta2khu75.thinkhub.notification.internal;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.comment.api.CommentApi;
import com.ta2khu75.thinkhub.notification.api.NotificationApi;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationIdDto;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationStatusRequest;
import com.ta2khu75.thinkhub.notification.internal.entity.Notification;
import com.ta2khu75.thinkhub.notification.internal.entity.NotificationId;
import com.ta2khu75.thinkhub.notification.internal.mapper.NotificationMapper;
import com.ta2khu75.thinkhub.notification.internal.repository.NotificationRepository;
import com.ta2khu75.thinkhub.post.api.PostApi;
import com.ta2khu75.thinkhub.quiz.api.QuizApi;
import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
public class NotificationServiceImpl
		extends BaseService<Notification, NotificationId, NotificationRepository, NotificationMapper>
		implements NotificationApi {
	private final PostApi postApi;
	private final QuizApi quizApi;
	private final CommentApi commentApi;
	private final ReportApi reportApi;
	private final ApplicationEventPublisher events;

	public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper, PostApi postApi,
			QuizApi quizApi, CommentApi commentApi, ReportApi reportApi, ApplicationEventPublisher events) {
		super(repository, mapper);
		this.events = events;
		this.postApi = postApi;
		this.quizApi = quizApi;
		this.reportApi = reportApi;
		this.commentApi = commentApi;
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
		Long userId = SecurityUtil.getCurrentUserIdDecode();
		Page<Notification> page = repository.findByIdUserId(userId, search.toPageable());
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
			return postApi.read(targetId);
		case QUIZ:
			return quizApi.read(targetId);
		case COMMENT:
			return commentApi.read(targetId);
		case REPORT:
			return reportApi.read(targetId);
		default:
			throw new IllegalArgumentException("Unexpected value: " + notification.getId().getTargetType());
		}
	}

	private NotificationIdDto toNotificationIdDto(NotificationId id) {
		String userId = IdConverterUtil.encode(id.getUserId(), IdConfig.USER);
		switch (id.getTargetType()) {
		case POST:
			String postId = IdConverterUtil.encode(id.getTargetId(), IdConfig.POST);
			return new NotificationIdDto(userId, postId, id.getTargetType());
		case QUIZ:
			String quizId = IdConverterUtil.encode(id.getTargetId(), IdConfig.QUIZ);
			return new NotificationIdDto(userId, quizId, id.getTargetType());
		case COMMENT:
			return new NotificationIdDto(userId, id.getTargetId().toString(), id.getTargetType());
		case REPORT:
			return new NotificationIdDto(userId, id.getTargetId().toString(), id.getTargetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + id.getTargetType());
		}
	}

	private NotificationId toNotificationId(NotificationIdDto id) {
		Long userId = IdConverterUtil.decode(id.accountId(), IdConfig.USER);
		switch (id.targetType()) {
		case POST:
			Long postId = IdConverterUtil.decode(id.targetId(), IdConfig.POST);
			return new NotificationId(userId, postId, id.targetType());
		case QUIZ:
			Long quizId = IdConverterUtil.decode(id.targetId(), IdConfig.QUIZ);
			return new NotificationId(userId, quizId, id.targetType());
		case COMMENT:
			return new NotificationId(userId, Long.valueOf(id.targetId()), id.targetType());
		case REPORT:
			return new NotificationId(userId, Long.valueOf(id.targetId()), id.targetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + id.targetType());
		}
	}
//	private final BlogService blogService;
//	private final quizApi quizApi;
//
//	public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper,
//			BlogService blogService, quizApi quizApi) {
//		super(repository, mapper);
//		this.blogService = blogService;
//		this.quizApi = quizApi;
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
//			return quizApi.read(Base62.encodeWithSalt(notification.getId().getTargetId(), SaltedType.QUIZ));
//		default:
//			throw new IllegalArgumentException("Unsupported target type: " + notification.getId().getTargetType());
//		}
//	}
}
