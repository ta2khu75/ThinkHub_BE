package com.ta2khu75.thinkhub.tag.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.tag.TagService;
import com.ta2khu75.thinkhub.tag.dto.TagDto;
import com.ta2khu75.thinkhub.tag.entity.Tag;
import com.ta2khu75.thinkhub.tag.mapper.TagMapper;
import com.ta2khu75.thinkhub.tag.repository.TagRepository;

import jakarta.validation.Valid;

@Service
public class TagServiceImpl extends BaseService<Tag, Long, TagRepository, TagMapper> implements TagService {

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

}
