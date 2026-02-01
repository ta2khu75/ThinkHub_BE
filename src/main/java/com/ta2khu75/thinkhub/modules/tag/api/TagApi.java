package com.ta2khu75.thinkhub.modules.tag.api;

import java.util.Set;

import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface TagApi extends ExistsService<Long> {
	TagDto create(TagDto request);

	TagDto readByName(String name);

	Set<TagDto> readAllByIds(Set<Long> ids);

	Set<TagDto> readAllByNameIn(Set<String> names);
}
