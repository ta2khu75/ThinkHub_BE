package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

public interface SearchService<RES,S extends Search> {
	PageResponse<RES> search(S search);
}
