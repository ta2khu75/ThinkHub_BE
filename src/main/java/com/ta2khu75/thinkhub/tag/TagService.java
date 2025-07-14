package com.ta2khu75.thinkhub.tag;

import java.util.Set;

import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

public interface TagService extends ExistsService<Long> {
	TagDto create(@Valid TagDto request);

	void delete(Long id);
	Set<TagDto> readAllByIds(Set<Long> ids);
}
