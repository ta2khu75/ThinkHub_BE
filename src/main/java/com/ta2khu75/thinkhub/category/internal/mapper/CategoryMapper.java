package com.ta2khu75.thinkhub.category.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.category.internal.entity.Category;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface CategoryMapper extends Converter<Category, CategoryResponse> {
	@Override
	CategoryResponse convert(Category source);

	@Mapping(target = "id", ignore = true)
	
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	Category toEntity(CategoryRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	void update(CategoryRequest request, @MappingTarget Category category);
}
