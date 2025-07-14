package com.ta2khu75.thinkhub.tag.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.tag.dto.TagDto;
import com.ta2khu75.thinkhub.tag.entity.Tag;

@Mapper(config = MapperSpringConfig.class)
public interface TagMapper extends Converter<Tag, TagDto> {
	@Override
	TagDto convert(Tag source);

	@Mapping(target = "createdAt", ignore = true)
	Tag toEntity(TagDto dto);
}
