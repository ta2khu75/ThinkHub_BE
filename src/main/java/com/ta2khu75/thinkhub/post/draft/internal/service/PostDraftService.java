package com.ta2khu75.thinkhub.post.draft.internal.service;

import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftCreateRequest;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftResponse;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftUpdateRequest;

public interface PostDraftService {
	PostDraftResponse create(PostDraftCreateRequest request);

	void update(String id, PostDraftUpdateRequest request);

	PostDraftResponse read(String id);

	void delete(String id);
}
