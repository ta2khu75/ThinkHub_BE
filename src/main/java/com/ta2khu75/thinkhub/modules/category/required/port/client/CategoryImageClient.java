package com.ta2khu75.thinkhub.modules.category.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.category.required.port.CategoryImagePort;
import com.ta2khu75.thinkhub.modules.media.api.MediaApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
class CategoryImageClient extends BaseClient<MediaApi> implements CategoryImagePort {

	protected CategoryImageClient(MediaApi api) {
		super(api);
	}

	@Override
	public String getUrl(Long id) {
		return api.readUrl(id);
	}

}
