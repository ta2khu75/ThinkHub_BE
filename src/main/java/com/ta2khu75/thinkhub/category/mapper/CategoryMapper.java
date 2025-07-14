package com.ta2khu75.thinkhub.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.category.dto.CategoryDto;
import com.ta2khu75.thinkhub.category.entity.Category;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface CategoryMapper extends Converter<Category, CategoryDto> {
	@Override
	CategoryDto convert(Category source);

	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	Category toEntity(CategoryDto dto);
}
