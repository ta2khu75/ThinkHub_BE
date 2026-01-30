package com.ta2khu75.thinkhub.quizDraft.internal;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.quiz.required.port.QuizMediaPort;
import com.ta2khu75.thinkhub.quiz.required.port.QuizTagPort;
import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.quizDraft.api.dto.QuizDraftUpdateRequest;
import com.ta2khu75.thinkhub.quizDraft.internal.domain.QuizDraft;
import com.ta2khu75.thinkhub.quizDraft.internal.infra.redis.QuizDraftCache;
import com.ta2khu75.thinkhub.quizDraft.internal.mapper.QuizDraftMapper;
import com.ta2khu75.thinkhub.quizDraft.internal.service.QuizDraftService;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
class QuizDraftServiceImpl implements QuizDraftService {

	public QuizDraftServiceImpl(QuizDraftMapper mapper, QuizTagPort tagPort, ApplicationEventPublisher events,
			QuizMediaPort mediaPort, QuizDraftCache cache) {
		this.events = events;
		this.mediaPort = mediaPort;
		this.mapper = mapper;
		this.cache = cache;
	}

	private final QuizDraftMapper mapper;
	private final QuizMediaPort mediaPort;
	private final QuizDraftCache cache;
	private final ApplicationEventPublisher events;

	@Override
	public QuizDraftResponse create(QuizDraftCreateRequest request) {
		String id = UUID.randomUUID().toString();
		QuizDraft quiz = mapper.create(request);
		Long ownerId = SecurityUtil.getCurrentUserIdDecode();
		quiz.setId(id);
		quiz.setOwnerId(ownerId);
		cache.save(id, quiz);
		return mapper.convert(quiz);
	}

	@Override
	public void update(String id, QuizDraftUpdateRequest request) {
		QuizDraft quiz = cache.get(id);
		if (quiz.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		mapper.update(request, quiz);
		cache.save(id, quiz);
	}

	@Override
	public QuizDraftResponse read(String id) {
		QuizDraft quiz = cache.get(id);
		if (quiz.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		return mapper.convert(quiz);
	}

	@Override
	public void delete(String id) {
		QuizDraft quiz = cache.get(id);
		if (quiz.getOwnerId() != SecurityUtil.getCurrentUserIdDecode())
			throw new IllegalArgumentException();
		cache.delete(id);
	}

}
