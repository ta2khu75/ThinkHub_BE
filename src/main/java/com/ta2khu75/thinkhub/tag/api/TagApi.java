package com.ta2khu75.thinkhub.tag.api;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

public interface TagApi extends ExistsService<Long> {
	TagDto create(TagDto request);

	TagDto readByName(String name);

	Set<TagDto> readAllByIds(Set<Long> ids);

	Set<TagDto> readAllByNameIn(Set<String> names);
}
