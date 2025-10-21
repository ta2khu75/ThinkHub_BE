package com.ta2khu75.thinkhub.category.required.port;

import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;

public interface CategoryMediaPort {
	MediaResponse read(Long id);
}
