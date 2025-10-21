package com.ta2khu75.thinkhub.post.required.client;

import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;

public interface PostMediaPort {
	MediaResponse read(Long id);
}
