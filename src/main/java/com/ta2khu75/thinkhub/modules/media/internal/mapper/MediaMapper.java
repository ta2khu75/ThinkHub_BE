package com.ta2khu75.thinkhub.modules.media.internal.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.modules.media.internal.domain.Media;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface MediaMapper extends Converter<Media, MediaResponse> {
	@Override
	MediaResponse convert(Media source);
}
