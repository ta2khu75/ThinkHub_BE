package com.ta2khu75.thinkhub.category.service;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.category.CategoryService;
import com.ta2khu75.thinkhub.category.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.dto.CategoryResponse;
import com.ta2khu75.thinkhub.category.entity.Category;
import com.ta2khu75.thinkhub.category.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.category.repository.CategoryRepository;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository, CategoryMapper>
		implements CategoryService {

	protected CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public CategoryResponse create(@Valid CategoryRequest request) {
		Category category = mapper.toEntity(request);
		return mapper.toResponse(repository.save(category));
	}

	@Override
	public CategoryResponse update(Long id, @Valid CategoryRequest request) {
		Category category = this.readEntity(id);
		mapper.update(request, category);
		return mapper.toResponse(repository.save(category));
	}

	@Override
	public CategoryResponse read(Long id) {
		return mapper.toResponse(this.readEntity(id));
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
