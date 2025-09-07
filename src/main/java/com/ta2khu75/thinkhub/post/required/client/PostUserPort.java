package com.ta2khu75.thinkhub.post.required.client;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public interface PostUserPort {
	AuthorResponse readAuthor(Long id);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> ids);
}
