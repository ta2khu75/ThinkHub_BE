package com.ta2khu75.thinkhub.tag.internal;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.tag.api.TagApi;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.tag.internal.entity.Tag;
import com.ta2khu75.thinkhub.tag.internal.mapper.TagMapper;
import com.ta2khu75.thinkhub.tag.internal.repository.TagRepository;

import jakarta.validation.Valid;

@Service
class TagServiceImpl extends BaseService<Tag, Long, TagRepository, TagMapper> implements TagApi {

	protected TagServiceImpl(TagRepository repository, TagMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public TagDto create(@Valid TagDto request) {
		Tag tag = mapper.toEntity(request);
		return mapper.convert(repository.save(tag));
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find tag with id: " + id);
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.TAG;
	}

	@Override
	public Set<TagDto> readAllByIds(Set<Long> ids) {
		return repository.findAllById(ids).stream().map(mapper::convert).collect(Collectors.toSet());
	}

	@Override
	public PageResponse<TagDto> search(Search search) {
		return mapper
				.toPageResponse(repository.findByNameContainingIgnoreCase(search.getKeyword(), search.toPageable()));
	}

	@Override
	public Set<TagDto> readAllByNameIn(Set<String> names) {
		return repository.findAllByNameIn(names).stream().map(mapper::convert).collect(Collectors.toSet());
	}

	@Override
	public TagDto readByName(String name) {
		return repository.findByName(name).map(mapper::convert).orElse(null);
	}
}
