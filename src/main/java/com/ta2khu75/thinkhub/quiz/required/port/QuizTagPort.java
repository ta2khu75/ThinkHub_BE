package com.ta2khu75.thinkhub.quiz.required.port;

import java.util.Set;

import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

public interface QuizTagPort {
	TagDto readByName(String name);
	Set<TagDto> readAllByNameIn(Set<String> names);
	TagDto create(String name);
	Set<TagDto> readAllByIds(Set<Long> ids);
}
