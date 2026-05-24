package com.ta2khu75.thinkhub.modules.tag.internal;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.tag.api.TagApi;
import com.ta2khu75.thinkhub.modules.tag.api.dto.TagDto;
import com.ta2khu75.thinkhub.modules.tag.internal.domain.Tag;
import com.ta2khu75.thinkhub.modules.tag.internal.mapper.TagMapper;
import com.ta2khu75.thinkhub.modules.tag.internal.repository.TagRepository;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
class TagService extends BaseService<Tag, Long, TagRepository> implements TagApi {

	protected TagService(TagRepository repository, TagMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private TagMapper mapper;

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

	@Override
	public void ensureExists(Long id) {
		this.assertExists(id);
	}
}
