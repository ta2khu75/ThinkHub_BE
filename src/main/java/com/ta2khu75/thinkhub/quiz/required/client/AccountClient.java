package com.ta2khu75.thinkhub.quiz.required.client;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public interface AccountClient {
	AuthorResponse readAuthor(Long id);

	Map<Long, AuthorResponse> readMapAuthorsByAccountIds(Collection<Long> ids);
}
