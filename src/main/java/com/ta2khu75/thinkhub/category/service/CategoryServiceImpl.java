package com.ta2khu75.thinkhub.category.service;

import org.springframework.stereotype.Service;
import com.ta2khu75.thinkhub.category.CategoryService;
import com.ta2khu75.thinkhub.category.dto.CategoryDto;
import com.ta2khu75.thinkhub.category.entity.Category;
import com.ta2khu75.thinkhub.category.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.category.repository.CategoryRepository;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository, CategoryMapper>
		implements CategoryService {

	protected CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public CategoryDto create(@Valid CategoryDto request) {
		Category category = mapper.toEntity(request);
		return mapper.convert(repository.save(category));
	}

	@Override
	public CategoryDto update(Long id, @Valid CategoryDto request) {
		Category category = mapper.toEntity(request);
		return mapper.convert(repository.save(category));
	}

	@Override
	public CategoryDto read(Long id) {
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
}
