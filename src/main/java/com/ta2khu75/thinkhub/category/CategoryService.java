package com.ta2khu75.thinkhub.category;

import java.util.List;

import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface CategoryService extends CrudService<CategoryRequest, CategoryResponse, Long>, ExistsService<Long> {
	List<CategoryResponse> readAll();
}
