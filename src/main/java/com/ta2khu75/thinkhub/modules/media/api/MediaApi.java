package com.ta2khu75.thinkhub.modules.media.api;

import java.io.IOException;

import com.ta2khu75.thinkhub.modules.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface MediaApi extends ExistsService<Long> {
	MediaResponse create(MediaRequest request) throws IOException;

	MediaResponse read(Long id);

	String readUrl(Long id);

	void delete(Long id);

	void attach(Long id);

	void detach(Long id);

}
