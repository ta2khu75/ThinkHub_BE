package com.ta2khu75.thinkhub.notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ta2khu75.quiz.mapper.NotificationMapper;
import com.ta2khu75.quiz.model.entity.Notification;
import com.ta2khu75.quiz.model.response.NotificationResponse;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.repository.NotificationRepository;
import com.ta2khu75.quiz.service.BlogService;
import com.ta2khu75.quiz.service.QuizService;
import com.ta2khu75.quiz.service.NotificationService;
import com.ta2khu75.quiz.service.base.BaseService;
import com.ta2khu75.quiz.util.Base62;
import com.ta2khu75.quiz.util.SaltedType;

@Service
public class NotificationServiceImpl extends BaseService<NotificationRepository, NotificationMapper>
		implements NotificationService {

	private final BlogService blogService;
	private final QuizService quizService;

	public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper,
			BlogService blogService, QuizService quizService) {
		super(repository, mapper);
		this.blogService = blogService;
		this.quizService= quizService;
	}

//	@Override
//	public Notification create(@Valid Notification request) {
//		return repository.save(request);
//	}
//
//	@Override
//	public Notification update(NotificationId id, @Valid Notification request) {
//		return null;
//	}
//
//	@Override
//	public Notification read(NotificationId id) {
//		return null;
//	}
//
//	@Override
//	public void delete(NotificationId id) {
//		repository.deleteById(id);
//	}

	@Override
	public PageResponse<NotificationResponse> readPageByAccountId(String accountId, Pageable pageable) {
		Page<Notification> page = repository.findByAccountId(accountId, pageable);
		PageResponse<NotificationResponse> response = mapper.toPageResponse(page);
		response.getContent().forEach(notification -> notification.setTarget(this.resolveTarget(notification)));
		return response;
	}
	private Object resolveTarget(NotificationResponse notification) {
		switch (notification.getId().getTargetType()) {
		case BLOG:
			return blogService.read(Base62.encodeWithSalt(notification.getId().getTargetId(), SaltedType.BLOG));
		case QUIZ:
			return quizService.read(Base62.encodeWithSalt(notification.getId().getTargetId(), SaltedType.QUIZ));
		default:
			throw new IllegalArgumentException("Unsupported target type: " + notification.getId().getTargetType());
		}
	}
}
