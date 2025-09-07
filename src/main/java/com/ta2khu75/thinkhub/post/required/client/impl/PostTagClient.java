package com.ta2khu75.thinkhub.post.required.client.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.post.required.client.PostTagPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.tag.api.TagApi;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;
@Component
public class PostTagClient extends BaseClient<TagApi> implements PostTagPort {

	protected PostTagClient(TagApi api) {
		super(api);
	}

	@Override
	public TagDto readByName(String name) {
		return api.readByName(name);
	}

	@Override
	public Set<TagDto> readAllByNameIn(Set<String> names) {
		return api.readAllByNameIn(names);
	}

	@Override
	public TagDto create(String name) {
		return api.create(new TagDto(null, name));
	}

	@Override
	public Set<TagDto> readAllByIds(Set<Long> ids) {
		return api.readAllByIds(ids);
	}

}
