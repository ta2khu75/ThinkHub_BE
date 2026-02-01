package com.ta2khu75.thinkhub.modules.category.internal;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.category.api.CategoryApi;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.modules.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.modules.category.internal.entity.Category;
import com.ta2khu75.thinkhub.modules.category.internal.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.modules.category.internal.repository.CategoryRepository;
import com.ta2khu75.thinkhub.modules.category.internal.service.CategoryService;
import com.ta2khu75.thinkhub.modules.category.required.port.CategoryMediaPort;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.domain.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository>
		implements CategoryService, CategoryApi {

	public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper, CategoryMediaPort mediaPort,
			ApplicationEventPublisher events) {
		super(repository);
		this.mediaPort = mediaPort;
		this.events = events;
		this.mapper = mapper;
	}

	private final CategoryMediaPort mediaPort;
	private final ApplicationEventPublisher events;
	private final CategoryMapper mapper;

	@Override
	public CategoryResponse create(@Valid CategoryRequest request) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.mediaId()));
		events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.defaultMediaId()));
		Category category = mapper.toEntity(request);
		return this.toResponse(repository.save(category));
	}

	@Override
	public CategoryResponse update(Long id, @Valid CategoryRequest request) {
		if (request.mediaId() != null) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.mediaId()));
		}
		if (request.defaultMediaId() != null) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.MEDIA, request.defaultMediaId()));
		}
		Category category = readEntity(id);
		mapper.update(request, category);
		return this.toResponse(repository.save(category));
	}

	@Override
	public CategoryResponse read(Long id) {
		return this.toResponse(readEntity(id));
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.CATEGORY;
	}

	@Override
	public List<CategoryResponse> readAll() {
		return repository.findAll().stream().map(this::toResponse).toList();
	}

	private CategoryResponse toResponse(Category category) {
		CategoryResponse response = mapper.convert(category);
		MediaResponse media = mediaPort.read(category.getMediaId());
		MediaResponse defaultMedia = mediaPort.read(category.getDefaultMediaId());
		response.setImageUrl(media.url());
		response.setDefaultImageUrl(defaultMedia.url());
		return response;
	}

	@Override
	public void ensureExists(Long id) {
		this.assertExists(id);
	}
}
