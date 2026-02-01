package com.ta2khu75.thinkhub.modules.quiz.draft.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.modules.quiz.draft.api.dto.QuizDraftUpdateRequest;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.domain.QuizDraft;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.infra.redis.QuizDraftCache;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.mapper.QuizDraftMapper;
import com.ta2khu75.thinkhub.modules.quiz.draft.internal.service.QuizDraftService;
import com.ta2khu75.thinkhub.modules.quiz.required.port.QuizMediaPort;
import com.ta2khu75.thinkhub.modules.quiz.required.port.QuizTagPort;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
class QuizDraftServiceImpl implements QuizDraftService {

	public QuizDraftServiceImpl(QuizDraftMapper mapper, QuizTagPort tagPort, QuizMediaPort mediaPort,
			QuizDraftCache cache) {
		this.mediaPort = mediaPort;
		this.mapper = mapper;
		this.cache = cache;
	}

	private final QuizDraftMapper mapper;
	private final QuizMediaPort mediaPort;
	private final QuizDraftCache cache;

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
