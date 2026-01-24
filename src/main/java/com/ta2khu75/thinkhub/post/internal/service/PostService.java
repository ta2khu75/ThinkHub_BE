package com.ta2khu75.thinkhub.post.internal.service;

import com.ta2khu75.thinkhub.post.api.dto.PostRequest;
import com.ta2khu75.thinkhub.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.SearchService;

public interface PostService extends CrudService<PostRequest, PostResponse, String>,
		SearchService<PostSearch, PostResponse>, ExistsService<String> {
	PostResponse readDetail(String id);
	void disable(String id);
}