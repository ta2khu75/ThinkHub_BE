package com.ta2khu75.thinkhub.tag;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

public interface TagService extends SearchService<Search, TagDto>,ExistsService<Long> {
	TagDto create(@Valid TagDto request);
	void delete(Long id);
	TagDto readByName(String name);
	Set<TagDto> readAllByIds(Set<Long> ids);
	Set<TagDto> readAllByNameIn(Set<String> names);
}
