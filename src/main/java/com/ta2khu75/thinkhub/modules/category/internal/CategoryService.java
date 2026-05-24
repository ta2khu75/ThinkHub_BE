package com.ta2khu75.thinkhub.modules.category.internal;

import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.category.api.CategoryApi;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.modules.category.internal.domain.Category;
import com.ta2khu75.thinkhub.modules.category.internal.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.modules.category.internal.repository.CategoryRepository;
import com.ta2khu75.thinkhub.modules.category.internal.validator.CategoryValidator;
import com.ta2khu75.thinkhub.modules.category.required.port.CategoryImagePort;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.domain.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.util.SlugUtil;

import jakarta.validation.Valid;

@Service
class CategoryService extends BaseService<Category, Long, CategoryRepository> implements CategoryApi {

	public CategoryService(CategoryRepository repository, CategoryMapper mapper, CategoryImagePort imagePort,
			CategoryValidator validator, ApplicationEventPublisher events) {
		super(repository);
		this.events = events;
		this.mapper = mapper;
		this.validator = validator;
		this.imagePort = imagePort;
	}

	private final CategoryImagePort imagePort;
	private final ApplicationEventPublisher events;
	private final CategoryMapper mapper;
	private final CategoryValidator validator;

	private CategoryResponse toResponse(Category category) {
		CategoryResponse response = mapper.convert(category);
		String imageUrl = imagePort.getUrl(category.getImageId());
		String imageFallbackUrl = imagePort.getUrl(category.getFallbackImageId());
		response.setImageUrl(imageUrl);
		response.setFallbackImageUrl(imageFallbackUrl);
		return response;
	}

	@Override
	public CategoryResponse create(@Valid CategoryRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.imageId()));
		events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.fallbackImageId()));
		String slug = SlugUtil.toSlug(request.name());
		Category category = Category.create(request.name(), slug, request.description(), request.imageId(),
				request.fallbackImageId());
		category = repository.save(category);
		return this.toResponse(category);
	}

	@Override
	public CategoryResponse update(Long id, @Valid CategoryRequest request) {
		String slug = SlugUtil.toSlug(request.name());
		Category category = readEntity(id);
		Long oldImageId = category.getImageId();
		Long oldFallbackImageId = category.getImageId();
		category.update(request.name(), slug, request.description(), request.imageId(), request.fallbackImageId());
		if (!Objects.equals(category.getImageId(), oldImageId)) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, category.getImageId()));
		}
		if (!Objects.equals(category.getFallbackImageId(), oldFallbackImageId)) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, category.getFallbackImageId()));
		}
		category = repository.save(category);
		return this.toResponse(category);
	}

	@Override
	public CategoryResponse read(Long id) {
		return this.toResponse(readEntity(id));
	}

	@Override
	public List<CategoryResponse> readAll() {
		return repository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.CATEGORY;
	}

	@Override
	public void ensureExists(Long id) {
		this.readEntity(id);
	}

	@Override
	public void delete(Long id) {
		Category category = readEntity(id);
		category.delete();
		category = repository.save(category);
		this.toResponse(category);
	}

	@Override
	public CategoryResponse activate(Long id) {
		Category category = this.readEntity(id);
		category.activate();
		category = repository.save(category);
		return toResponse(category);
	}

	@Override
	public CategoryResponse deactivate(Long id) {
		Category category = this.readEntity(id);
		category.deactivate();
		category = repository.save(category);
		return toResponse(category);
	}
}
