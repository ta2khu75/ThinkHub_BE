package com.ta2khu75.thinkhub.post.api;

import com.ta2khu75.thinkhub.post.api.dto.PostRequest;
import com.ta2khu75.thinkhub.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface PostApi extends CrudService<PostRequest, PostResponse, Long>, SearchService<PostSearch, PostResponse>,
		ExistsService<Long> {
	PostResponse readDetail(Long id);

}
