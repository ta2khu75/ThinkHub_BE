package com.ta2khu75.thinkhub.media.internal.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.media.internal.entity.Media;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface MediaMapper extends Converter<Media, MediaResponse> {
	@Override
	MediaResponse convert(Media source);
}
