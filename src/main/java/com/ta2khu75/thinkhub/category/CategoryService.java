package com.ta2khu75.thinkhub.category;

import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface CategoryService extends CrudService<CategoryRequest, CategoryResponse, Long> {

}
