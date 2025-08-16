package com.ta2khu75.thinkhub.quiz.required.client.impl;

import java.util.Set;

import com.ta2khu75.thinkhub.quiz.required.client.TagClient;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.tag.api.TagApi;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

public class TagClientImpl extends BaseClient<TagApi> implements TagClient {

	protected TagClientImpl(TagApi api) {
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
