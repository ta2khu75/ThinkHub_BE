package com.ta2khu75.thinkhub.post.repository;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.post.entity.Post;

public interface PostRepositoryCustom {
	Page<Post> search(PostSearch search);
}