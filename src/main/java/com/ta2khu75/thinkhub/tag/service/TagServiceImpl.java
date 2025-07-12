package com.ta2khu75.thinkhub.tag.service;

import org.springframework.stereotype.Service;

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

}
