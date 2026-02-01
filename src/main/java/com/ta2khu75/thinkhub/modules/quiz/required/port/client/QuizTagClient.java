package com.ta2khu75.thinkhub.modules.quiz.required.port.client;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.quiz.required.port.QuizTagPort;
import com.ta2khu75.thinkhub.modules.tag.api.TagApi;
import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;

@Component
public class QuizTagClient extends BaseClient<TagApi> implements QuizTagPort {

	protected QuizTagClient(TagApi api) {
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
