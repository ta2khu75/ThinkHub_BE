package com.ta2khu75.thinkhub.modules.tag.api;

import java.util.Set;

import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface TagApi extends ExistsService<Long>, SearchService<Search, TagDto> {
	TagDto create(TagDto request);

	TagDto readByName(String name);

	Set<TagDto> readAllByIds(Set<Long> ids);

	Set<TagDto> readAllByNameIn(Set<String> names);

	void delete(Long id);

}
