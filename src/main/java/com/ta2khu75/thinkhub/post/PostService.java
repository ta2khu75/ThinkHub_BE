package com.ta2khu75.thinkhub.post;

import com.ta2khu75.thinkhub.post.dto.PostRequest;
import com.ta2khu75.thinkhub.post.dto.PostResponse;
import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface PostService extends CrudService<PostRequest, PostResponse, Long>,
		SearchService<PostSearch, PostResponse>, ExistsService<Long> {
	PostResponse readDetail(Long id);
		
}
