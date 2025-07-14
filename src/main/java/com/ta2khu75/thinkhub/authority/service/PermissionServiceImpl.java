package com.ta2khu75.thinkhub.authority.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authority.PermissionService;
import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.mapper.PermissionMapper;
import com.ta2khu75.thinkhub.authority.repository.PermissionRepository;
import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class PermissionServiceImpl extends BaseService<Permission, Long, PermissionRepository, PermissionMapper>
		implements PermissionService {

	protected PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public PermissionResponse create(@Valid PermissionRequest request) {
		Permission permission = mapper.toEntity(request);
		permission = repository.save(permission);
		return mapper.convert(permission);
	}

	@Override
	public PermissionResponse update(Long id, @Valid PermissionRequest request) {
		Permission permission = this.readEntity(id);
		mapper.update(request, permission);
		permission = repository.save(permission);
		return mapper.convert(permission);
	}

	@Override
	public PermissionResponse read(Long id) {
		Permission permission = this.readEntity(id);
		return mapper.convert(permission);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Set<PermissionResponse> readAllBySummary(Set<String> summaries) {
		return repository.findAllBySummaryIn(summaries).stream().map(mapper::convert).collect(Collectors.toSet());
	}

	@Override
	public PermissionResponse readBySummary(String summary) {
		return mapper.convert(repository.findBySummary(summary)
				.orElseThrow(() -> new NotFoundException("Could not find Permission with summary: " + summary)));
	}

	@Override
	public Optional<PermissionResponse> findBySummary(String summary) {

		return repository.findBySummary(summary).map(mapper::convert);
	}

}
