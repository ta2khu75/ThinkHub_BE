package com.ta2khu75.thinkhub.modules.post.internal.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.modules.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.modules.post.internal.entity.Post;

public interface PostRepositoryCustom {
	Page<Post> search(PostSearch search);
}