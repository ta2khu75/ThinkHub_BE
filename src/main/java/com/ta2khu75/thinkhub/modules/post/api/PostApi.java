package com.ta2khu75.thinkhub.modules.post.api;

import com.ta2khu75.thinkhub.modules.post.api.dto.PostResponse;

public interface PostApi {
	PostResponse read(Long id);
}
