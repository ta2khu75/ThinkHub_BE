package com.ta2khu75.thinkhub.post.required.client;

import java.util.Set;

import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

public interface PostTagPort {
	TagDto readByName(String name);
	Set<TagDto> readAllByNameIn(Set<String> names);
	TagDto create(String name);
	Set<TagDto> readAllByIds(Set<Long> ids);
}
