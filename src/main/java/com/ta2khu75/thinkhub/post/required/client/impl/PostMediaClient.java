package com.ta2khu75.thinkhub.post.required.client.impl;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.media.api.MediaApi;
import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.post.required.client.PostMediaPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;

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
