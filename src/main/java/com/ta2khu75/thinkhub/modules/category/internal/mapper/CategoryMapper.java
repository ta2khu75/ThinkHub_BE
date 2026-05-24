package com.ta2khu75.thinkhub.modules.category.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.modules.category.internal.domain.Category;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface CategoryMapper extends Converter<Category, CategoryResponse> {
	@Override
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "fallbackImageUrl", ignore = true)
	CategoryResponse convert(Category source);
}
