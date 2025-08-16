package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

public interface SearchService<S extends Search, RES> {
	PageResponse<RES> search(S search);
}
