package com.ta2khu75.thinkhub.category.internal;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.category.api.CategoryApi;
import com.ta2khu75.thinkhub.category.api.dto.CategoryRequest;
import com.ta2khu75.thinkhub.category.api.dto.CategoryResponse;
import com.ta2khu75.thinkhub.category.internal.entity.Category;
import com.ta2khu75.thinkhub.category.internal.mapper.CategoryMapper;
import com.ta2khu75.thinkhub.category.internal.repository.CategoryRepository;
import com.ta2khu75.thinkhub.category.required.port.CategoryMediaPort;
import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl extends BaseService<Category, Long, CategoryRepository, CategoryMapper>
		implements CategoryApi {

	private final CategoryMediaPort mediaPort;
	private final ApplicationEventPublisher events;

	public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper, CategoryMediaPort mediaPort,
			ApplicationEventPublisher events) {
		super(repository, mapper);
		this.mediaPort = mediaPort;
		this.events = events;
	}

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
}
