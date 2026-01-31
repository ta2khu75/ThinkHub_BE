package com.ta2khu75.thinkhub.tag.internal.service;

import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.service.SearchService;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

public interface TagService extends SearchService<Search, TagDto> {
	void delete(Long id);
}
