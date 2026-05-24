package com.ta2khu75.thinkhub.modules.post.draft.api;

import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftCreateRequest;
import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftResponse;
import com.ta2khu75.thinkhub.modules.post.draft.api.dto.PostDraftUpdateRequest;

public interface PostDraftApi {
	PostDraftResponse create(PostDraftCreateRequest request);

	void update(String id, PostDraftUpdateRequest request);

	PostDraftResponse read(String id);

	void delete(String id);
}
