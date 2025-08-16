package com.ta2khu75.thinkhub.post.internal.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.post.api.dto.PostSearch;
import com.ta2khu75.thinkhub.post.internal.entity.Post;

public interface PostRepositoryCustom {
	Page<Post> search(PostSearch search);
}