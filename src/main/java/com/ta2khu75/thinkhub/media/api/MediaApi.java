package com.ta2khu75.thinkhub.media.api;

import java.io.IOException;

import com.ta2khu75.thinkhub.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface MediaApi extends ExistsService<Long> {
	MediaResponse create(MediaRequest request) throws IOException;

	MediaResponse update(Long id, MediaRequest request) throws IOException;

	MediaResponse read(Long id);

	void delete(Long id);
}
