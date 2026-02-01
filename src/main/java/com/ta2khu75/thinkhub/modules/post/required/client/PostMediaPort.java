package com.ta2khu75.thinkhub.modules.post.required.client;

import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;

public interface PostMediaPort {
	MediaResponse read(Long id);
}
