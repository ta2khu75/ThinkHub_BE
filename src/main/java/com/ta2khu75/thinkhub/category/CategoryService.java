package com.ta2khu75.thinkhub.category;

import java.util.List;

import com.ta2khu75.thinkhub.category.dto.CategoryDto;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface CategoryService extends CrudService<CategoryDto, CategoryDto, Long>, ExistsService<Long> {
	List<CategoryDto> readAll();
}
