package com.ta2khu75.thinkhub.modules.tag.internal.service;

import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface TagService extends SearchService<Search, TagDto> {
	void delete(Long id);
}
