package com.ta2khu75.thinkhub.post.api;

import com.ta2khu75.thinkhub.post.api.dto.PostResponse;

public interface PostApi {
	PostResponse read(Long id);
}
