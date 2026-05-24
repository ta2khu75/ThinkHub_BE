package com.ta2khu75.thinkhub.modules.tag.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.modules.tag.internal.domain.Tag;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface TagMapper extends Converter<Tag, TagDto>, PageMapper<Tag, TagDto> {
	@Override
	TagDto convert(Tag source);

	@Mapping(target = "createdAt", ignore = true)
	Tag toEntity(TagDto dto);
}
