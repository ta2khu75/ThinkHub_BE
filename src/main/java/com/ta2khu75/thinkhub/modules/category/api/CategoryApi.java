package com.ta2khu75.thinkhub.modules.category.api;

import java.util.List;

import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface CategoryApi extends CrudService<CategoryRequest, CategoryResponse, Long>, ExistsService<Long> {
	List<CategoryResponse> readAll();
	CategoryResponse activate(Long id);
	CategoryResponse deactivate(Long id);
}
