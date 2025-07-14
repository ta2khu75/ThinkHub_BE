package com.ta2khu75.thinkhub.post.listener;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.post.PostService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostListener {
	private final PostService service;
	
}
