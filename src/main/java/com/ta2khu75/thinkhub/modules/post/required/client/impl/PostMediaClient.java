package com.ta2khu75.thinkhub.modules.post.required.client.impl;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.media.api.MediaApi;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.modules.post.required.client.PostMediaPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
public class PostMediaClient extends BaseClient<MediaApi> implements PostMediaPort {

	protected PostMediaClient(MediaApi api) {
		super(api);
	}

	@Override
	public MediaResponse read(Long id) {
		return api.read(id);
	}

}
