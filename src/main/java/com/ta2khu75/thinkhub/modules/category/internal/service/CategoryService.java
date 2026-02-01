package com.ta2khu75.thinkhub.modules.category.internal.service;

import java.util.List;

import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface CategoryService extends CrudService<CategoryRequest, CategoryResponse, Long> {
	List<CategoryResponse> readAll();
}
