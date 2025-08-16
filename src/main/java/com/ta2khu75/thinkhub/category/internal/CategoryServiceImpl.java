package com.ta2khu75.thinkhub.category.internal;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.category.api.CategoryApi;
import com.ta2khu75.thinkhub.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.category.internal.entity.Category;
import com.ta2khu75.thinkhub.category.internal.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.category.internal.repository.CategoryRepository;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository, CategoryMapper>
		implements CategoryApi {

	protected CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public CategoryResponse create(@Valid CategoryRequest request) {
		Category category = mapper.toEntity(request);
		return mapper.convert(repository.save(category));
	}

	@Override
	public CategoryResponse update(Long id, @Valid CategoryRequest request) {
		Category category = readEntity(id);
		mapper.update(request, category);
		return mapper.convert(repository.save(category));
	}

	@Override
	public CategoryResponse read(Long id) {
		return mapper.convert(readEntity(id));
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find category with id: " + id);
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.CATEGORY;
	}

	@Override
	public List<CategoryResponse> readAll() {
		return repository.findAll().stream().map(mapper::convert).toList();
	}
}
