package com.ta2khu75.thinkhub.tag;

import com.ta2khu75.thinkhub.tag.dto.TagDto;

import jakarta.validation.Valid;

public interface TagService {
	TagDto create(@Valid TagDto request);

	void delete(Long id);
}
