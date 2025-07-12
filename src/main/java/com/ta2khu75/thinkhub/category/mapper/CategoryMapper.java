package com.ta2khu75.thinkhub.category.mapper;

import org.mapstruct.Mapper;

import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.category.entity.Category;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;
@Mapper(config = MapperSpringConfig.class)
public interface CategoryMapper extends BaseMapper<CategoryRequest, CategoryResponse, Category> {

}
