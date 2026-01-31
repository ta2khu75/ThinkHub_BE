package com.ta2khu75.thinkhub.comment.required.port;

import java.util.Collection;
import java.util.Map;

import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

public interface CommentUserPort {
	AuthorResponse readAuthor(String id);

	AuthorResponse readAuthor(Long id);

	Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds);
}
