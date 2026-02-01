package com.ta2khu75.thinkhub.modules.category.required.port;

import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;

public interface CategoryMediaPort {
	MediaResponse read(Long id);
}
